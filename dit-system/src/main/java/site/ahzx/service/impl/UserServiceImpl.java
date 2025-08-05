package site.ahzx.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.entity.SysMenus;
import site.ahzx.domain.entity.SysRoles;
import site.ahzx.domain.vo.LoginGetUserInfoVO;
import site.ahzx.domain.vo.SysUserNoPassVO;
import site.ahzx.domain.vo.SysUserVO;
import site.ahzx.domain.entity.SysUsers;
import site.ahzx.flex.context.LoginContext;
import site.ahzx.mapper.SysUsersMapper;
import site.ahzx.service.UserService;
import site.ahzx.util.R;
import site.ahzx.util.TableDataInfo;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static site.ahzx.constant.RedisCachePrefix.*;
import static site.ahzx.domain.entity.table.SysMenusTableDef.SYS_MENUS;
import static site.ahzx.domain.entity.table.SysRoleMenusTableDef.SYS_ROLE_MENUS;
import static site.ahzx.domain.entity.table.SysRolesTableDef.SYS_ROLES;
import static site.ahzx.domain.entity.table.SysUserRolesTableDef.SYS_USER_ROLES;
import static site.ahzx.domain.entity.table.SysUsersTableDef.SYS_USERS;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final SysUsersMapper sysUsersMapper;


    private final RedisTemplate<String, Object> redisTemplate;

    public UserServiceImpl(SysUsersMapper sysUsersMapper, RedisTemplate<String, Object> redisTemplate) {
        this.sysUsersMapper = sysUsersMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public SysUsers getUserByUsername(String username) {
        log.info("username: {}", username);
//        return SysUsers.create().where("username = ?", username).one();
        return SysUsers.create().where(SysUsers::getUsername).eq(username).one();
    }

    @Override
    public SysUserVO getUserDtoByUsername(String username) {

        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(SYS_USERS.ID.as("user_id"),SYS_USERS.TENANT_ID,SYS_USERS.USERNAME,SYS_USERS.PASSWORD,SYS_ROLES.ROLE_NAME,SYS_MENUS.PERMS)
                        .from(SYS_USERS)
                        .leftJoin(SYS_USER_ROLES).on(SYS_USERS.ID.eq(SYS_USER_ROLES.USER_ID))
                        .leftJoin(SYS_ROLES).on(SYS_USER_ROLES.ROLE_ID.eq(SYS_ROLES.ID))
                        .leftJoin(SYS_ROLE_MENUS).on(SYS_ROLES.ID.eq(SYS_ROLE_MENUS.ROLE_ID))
                        .leftJoin(SYS_MENUS).on(SYS_ROLE_MENUS.MENU_ID.eq(SYS_MENUS.ID))
                .where(SYS_USERS.USERNAME.eq(username));
        List<Row> rows= sysUsersMapper.selectRowsByQuery(queryWrapper);

        if (rows.isEmpty()) {
            return null;
        }

        SysUserVO dto = new SysUserVO();
        Set<String> roles = new HashSet<>();
        Set<String> perms = new HashSet<>();

        for (Row row : rows) {
            if (dto.getTenantId() == null) {
                dto.setUserId(row.getLong("user_id"));
                dto.setTenantId(row.getLong("tenant_id"));
                dto.setUsername(row.getString("username"));
                dto.setPassword(row.getString("password"));
            }

            String role = row.getString("role_name");
            if (role != null && !role.trim().isEmpty() ) {
                roles.add(role);
            }

            String perm = row.getString("perms");
            if (perm != null && !perm.trim().isEmpty()) {
                perms.add(perm);
            }
        }

        dto.setRoles(new ArrayList<>(roles));
        dto.setPermissions(new ArrayList<>(perms));
        log.info("dto is {}", dto);
        return dto;
    }

    @Override
    public LoginGetUserInfoVO getLoginUserInfo(String username) {

        QueryWrapper queryWrapper = QueryWrapper.create()
                        .select().from(SYS_USERS).where(SYS_USERS.USERNAME.eq(username));
        SysUsers sysUser = sysUsersMapper.selectOneWithRelationsByQuery(queryWrapper);

        log.debug("sysUser is {}", sysUser);
        sysUser.setPassword(null);


        // 去重收集角色 code 和菜单对象
        Set<String> roleCodes = new HashSet<>();
        Set<SysMenus> menuSet = new HashSet<>();

        for (SysRoles role : sysUser.getRoles()) {
            roleCodes.add(role.getRoleCode());

            if (role.getMenus() != null) {
                menuSet.addAll(role.getMenus()); // Set 会自动去重
            }
        }
        boolean isAdmin = roleCodes.stream().anyMatch("admin"::equalsIgnoreCase);
        if (isAdmin) {
//            roleCodes.clear();
//            roleCodes.add("admin");
            menuSet.clear();
            menuSet.addAll(SysMenus.create().where(SysMenus::getMenuType).in("M","C").list());
        }
        log.debug("menuSet is {}", menuSet);


// 从去重后的菜单中提取 perms
        Set<String> permissions = menuSet.stream()
                .map(SysMenus::getPerms)
                .filter(perm -> perm != null && !perm.isEmpty())
                .collect(Collectors.toSet());

        LoginGetUserInfoVO loginGetUserInfoVO = new LoginGetUserInfoVO();

// 设置结果
        loginGetUserInfoVO.setRoles(new ArrayList<>(roleCodes));
        loginGetUserInfoVO.setPermissions(new ArrayList<>(permissions));
        loginGetUserInfoVO.setUser(sysUser);
//        sysUser.getRoles().forEach(
//                role -> {
//                    loginGetUserInfoVO.getRoles().add(role.getRoleCode());
//                    role.getMenus().forEach(
//                            menu -> loginGetUserInfoVO.getPermissions().add(menu.getPerms())
//                    );
//                }
//
//        );
        // 写入 Redis
        redisTemplate.opsForValue()
                .set(USER_ROLES_PREFIX+ LoginContext.get().getTenantId()+":" + sysUser.getId(),
                loginGetUserInfoVO.getRoles(),
                        Duration.ofHours(2));
        redisTemplate.opsForValue()
                .set(USER_PERMS_PREFIX+ LoginContext.get().getTenantId()+":" + sysUser.getId(),
                        loginGetUserInfoVO.getPermissions(),
                        Duration.ofHours(2));
        redisTemplate.opsForValue().set(
                USER_MENUS_PREFIX + LoginContext.get().getTenantId()+":" + sysUser.getId(),
                new ArrayList<>(menuSet),
                Duration.ofHours(2)
        );


        return loginGetUserInfoVO;
    }

    @Override
    public TableDataInfo<SysUserNoPassVO> getUserList(PageBO pageBO) {
//        Page<SysUserNoPassVO> page = SysUsers.create().page(Page.of(pageBO.getPageNum(), pageBO.getPageSize()));

        Page<SysUserNoPassVO> usersPage = sysUsersMapper.paginateAs(1, 2, QueryWrapper.create().select().from(SYS_USERS), SysUserNoPassVO.class);

        return TableDataInfo.build(usersPage);

    }


}
