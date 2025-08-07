package site.ahzx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysUserBO;
import site.ahzx.domain.entity.SysMenu;
import site.ahzx.domain.entity.SysRole;
import site.ahzx.domain.vo.LoginGetUserInfoVO;
import site.ahzx.domain.vo.SysUserNoPassVO;
import site.ahzx.domain.vo.SysUserVO;
import site.ahzx.domain.entity.SysUser;
import site.ahzx.flex.context.LoginContext;
import site.ahzx.mapper.SysUsersMapper;
import site.ahzx.service.UserService;
import site.ahzx.util.TableDataInfo;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static site.ahzx.constant.RedisCachePrefix.*;
import static site.ahzx.domain.entity.table.SysMenuTableDef.SYS_MENU;
import static site.ahzx.domain.entity.table.SysRoleMenuTableDef.SYS_ROLE_MENU;
import static site.ahzx.domain.entity.table.SysRoleTableDef.SYS_ROLE;
import static site.ahzx.domain.entity.table.SysUserRoleTableDef.SYS_USER_ROLE;
import static site.ahzx.domain.entity.table.SysUserTableDef.SYS_USER;


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
    public SysUser getUserByUsername(String username) {
        log.info("username: {}", username);
//        return SysUsers.create().where("username = ?", username).one();
        return SysUser.create().where(SysUser::getUserName).eq(username).one();
    }

    @Override
    public SysUserVO getUserDtoByUsername(String username) {

        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(SYS_USER.USER_ID.as("user_id"),SYS_USER.TENANT_ID,SYS_USER.USER_NAME,SYS_USER.PASSWORD,
                        SYS_ROLE.ROLE_NAME,SYS_MENU.PERMS)
                        .from(SYS_USER)
                        .leftJoin(SYS_USER_ROLE).on(SYS_USER.USER_ID.eq(SYS_USER_ROLE.USER_ID))
                        .leftJoin(SYS_ROLE).on(SYS_USER_ROLE.ROLE_ID.eq(SYS_ROLE.ROLE_ID))
                        .leftJoin(SYS_ROLE_MENU).on(SYS_ROLE.ROLE_ID.eq(SYS_ROLE_MENU.ROLE_ID))
                        .leftJoin(SYS_MENU).on(SYS_ROLE_MENU.MENU_ID.eq(SYS_MENU.MENU_ID))
                .where(SYS_USER.USER_NAME.eq(username));
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
                dto.setUserName(row.getString("username"));
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
                        .select().from(SYS_USER).where(SYS_USER.USER_NAME.eq(username));
        SysUser sysUser = sysUsersMapper.selectOneWithRelationsByQuery(queryWrapper);

        log.debug("sysUser is {}", sysUser);
        sysUser.setPassword(null);


        // 去重收集角色 code 和菜单对象
        Set<String> roleCodes = new HashSet<>();
        Set<SysMenu> menuSet = new HashSet<>();

        for (SysRole role : sysUser.getRoles()) {
            roleCodes.add(role.getRoleKey());

            if (role.getMenus() != null) {
                log.debug("role.getMenus() is {}", role.getMenus());
                menuSet.addAll(role.getMenus()); // Set 会自动去重
            }
        }
        log.debug("roleCodes is {}", roleCodes);
        boolean isAdmin = roleCodes.stream().anyMatch("superadmin"::equalsIgnoreCase);

        log.debug("isAdmin is {}", isAdmin);
        if (isAdmin) {
//            roleCodes.clear();
//            roleCodes.add("admin");
            menuSet.clear();
            menuSet.addAll(SysMenu.create().where(SysMenu::getMenuType).in("M","C").list());

        }
        log.debug("menuSet is {}", menuSet);


// 从去重后的菜单中提取 perms
        Set<String> permissions = menuSet.stream()
                .map(SysMenu::getPerms)
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
                .set(USER_ROLE_PREFIX+ LoginContext.get().getTenantId()+":" + sysUser.getUserId(),
                loginGetUserInfoVO.getRoles(),
                        Duration.ofHours(2));
        redisTemplate.opsForValue()
                .set(USER_PERM_PREFIX+ LoginContext.get().getTenantId()+":" + sysUser.getUserId(),
                        loginGetUserInfoVO.getPermissions(),
                        Duration.ofHours(2));
        redisTemplate.opsForValue().set(
                USER_MENU_PREFIX + LoginContext.get().getTenantId()+":" + sysUser.getUserId(),
                new ArrayList<>(menuSet),
                Duration.ofHours(2)
        );


        return loginGetUserInfoVO;
    }

    @Override
    public TableDataInfo<SysUserNoPassVO> getUserList(PageBO pageBO) {
//        Page<SysUserNoPassVO> page = SysUsers.create().page(Page.of(pageBO.getPageNum(), pageBO.getPageSize()));

        Page<SysUserNoPassVO> usersPage = sysUsersMapper.paginateAs(1, 2, QueryWrapper.create().select().from(SYS_USER), SysUserNoPassVO.class);

        return TableDataInfo.build(usersPage);

    }

    @Override
    public Boolean checkUserExist(String username) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_USER.USER_NAME.eq(username));

        long count = sysUsersMapper.selectCountByQuery(query);
        return count > 0;
    }

    @Override
    public Boolean checkPhoneExist(String phone) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_USER.PHONE_NUMBER.eq(phone));

        long count = sysUsersMapper.selectCountByQuery(query);
        return count > 0;
    }

    @Override
    public Boolean checkEmailExist(String email) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_USER.EMAIL.eq(email));

        long count = sysUsersMapper.selectCountByQuery(query);
        return count > 0;
    }

    @Override
    public Integer addUser(SysUserBO userBO) {
        SysUser user = BeanUtil.copyProperties(userBO, SysUser.class);
        return sysUsersMapper.insert(user);
    }


}
