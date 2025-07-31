package site.ahzx.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.ahzx.domain.vo.LoginGetUserInfoVO;
import site.ahzx.domain.vo.SysUserVO;
import site.ahzx.domain.entity.SysUsers;
import site.ahzx.mapper.SysUsersMapper;
import site.ahzx.service.UserService;

import java.util.*;

import static site.ahzx.domain.entity.table.SysMenusTableDef.SYS_MENUS;
import static site.ahzx.domain.entity.table.SysRoleMenusTableDef.SYS_ROLE_MENUS;
import static site.ahzx.domain.entity.table.SysRolesTableDef.SYS_ROLES;
import static site.ahzx.domain.entity.table.SysUserRolesTableDef.SYS_USER_ROLES;
import static site.ahzx.domain.entity.table.SysUsersTableDef.SYS_USERS;
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUsersMapper sysUsersMapper;

    @Override
    public SysUsers getUserByUsername(String username) {
        log.info("username: {}", username);
//        return SysUsers.create().where("username = ?", username).one();
        return SysUsers.create().where(SysUsers::getUsername).eq(username).one();
    }

    @Override
    public SysUserVO getUserDtoByUsername(String username) {

        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(SYS_USERS.TENANT_ID,SYS_USERS.USERNAME,SYS_USERS.PASSWORD,SYS_ROLES.ROLE_NAME,SYS_MENUS.PERMS)
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
                        .select().where(SYS_USERS.USERNAME.eq(username));
        SysUsers sysUser = sysUsersMapper.selectOneWithRelationsByQuery(queryWrapper);
        sysUser.setPassword(null);

        LoginGetUserInfoVO loginGetUserInfoVO = new LoginGetUserInfoVO();
        loginGetUserInfoVO.setUser(sysUser);
        sysUser.getRoles().forEach(
                role -> {
                    loginGetUserInfoVO.getRoles().add(role.getRoleCode());
                    role.getMenus().forEach(
                            menu -> loginGetUserInfoVO.getPermissions().add(menu.getPerms())
                    );
                }

        );

        return loginGetUserInfoVO;
    }

}
