package site.ahzx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.mybatisflex.core.activerecord.query.RelationsQuery;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import site.ahzx.constant.SystemConstants;
import site.ahzx.domain.bo.SysMenuBO;
import site.ahzx.domain.entity.SysMenu;
import site.ahzx.domain.entity.SysRole;
import site.ahzx.domain.entity.SysUser;
import site.ahzx.domain.vo.RouterVO;
import site.ahzx.domain.vo.SysMenuVO;
import site.ahzx.flex.context.LoginContext;
import site.ahzx.mapper.SysMenusMapper;
import site.ahzx.service.MenuService;
import site.ahzx.constant.RedisCachePrefix;
import site.ahzx.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static site.ahzx.domain.entity.table.SysMenuTableDef.SYS_MENU;
import static site.ahzx.domain.entity.table.SysUserTableDef.SYS_USER;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SysMenusMapper sysMenuMapper;

    @Override
    public List<RouterVO> getRouterByUserId(Long userId) {
        try {
            String key = RedisCachePrefix.USER_MENU_PREFIX + LoginContext.get().getTenantId() + ":" + userId;
            @SuppressWarnings("unchecked")
            List<SysMenu> cachedMenus = (List<SysMenu>) redisTemplate.opsForValue().get(key);

            List<SysMenu> menus;
            if (cachedMenus != null && !cachedMenus.isEmpty()) {
                menus = cachedMenus;
            } else {
                // TODO: 从数据库查询用户权限菜单
                menus = sysMenuMapper.selectMenusByUserId(userId);
                // 缓存
                redisTemplate.opsForValue().set(key, menus, Duration.ofHours(3));
            }
            log.debug("menus are : {}",menus);
            // 构建 RouterVO 树结构
            List<RouterVO> routerList = MenuRouterBuilder.buildMenus(menus);
            log.debug("routerList is : {}",routerList);

            return routerList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SysMenuVO> selectMenuList(SysMenuBO menu, Long userId) {

        if (SystemConstants.SUPER_ADMIN_ID.equals(userId)){
            //管理员返回全部
            // 管理员返回所有菜单
            QueryWrapper queryWrapper = QueryWrapper.create().from(SysMenu.class);
            if(StringUtils.isNotBlank(menu.getMenuName())){
                queryWrapper.like(SysMenu::getMenuName,menu.getMenuName());
            }
            if (StringUtils.isNotBlank(menu.getMenuType())){
                queryWrapper.eq(SysMenu::getMenuType,menu.getMenuType());
            }
            if (StringUtils.isNotBlank(menu.getVisible())){
                queryWrapper.eq(SysMenu::getVisible,menu.getVisible());
            }
            if (StringUtils.isNotBlank(menu.getStatus())){
                queryWrapper.eq(SysMenu::getStatus,menu.getStatus());
            }
            queryWrapper.orderBy(SysMenu::getParentId, true);
            queryWrapper.orderBy(SysMenu::getOrderNum, true);




            List<SysMenu> menus = sysMenuMapper.selectListByQuery(queryWrapper);
            return BeanUtil.copyToList(menus, SysMenuVO.class);

        }
        else {
            // 非管理员根据角色查询
            RelationsQuery<SysUser> sysUserRelationsQuery = SysUser.create()
                    .where(SYS_USER.USER_ID.eq(userId))
                    .withRelations()
                    .maxDepth(3) //递归三层
                    .ignoreRelations("posts")
                    .ignoreRelations("dept");

            SysUser user = sysUserRelationsQuery.one();

            if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) {
                return Collections.emptyList();
            }

            // 从用户的角色中提取所有菜单
            Set<SysMenu> menuSet = new HashSet<>();
            for (SysRole role : user.getRoles()) {
                if (role.getMenus() != null) {
                    menuSet.addAll(role.getMenus());
                }
            }

            List<SysMenu> allMenus = new ArrayList<>(menuSet);

            // 在内存中过滤条件
            Stream<SysMenu> menuStream = allMenus.stream();

            if(StringUtils.isNotBlank(menu.getMenuName())){
                menuStream = menuStream.filter(m -> m.getMenuName() != null &&
                        m.getMenuName().contains(menu.getMenuName()));
            }
            if (StringUtils.isNotBlank(menu.getMenuType())){
                menuStream = menuStream.filter(m -> menu.getMenuType().equals(m.getMenuType()));
            }
            if (StringUtils.isNotBlank(menu.getVisible())){
                menuStream = menuStream.filter(m -> menu.getVisible().equals(m.getVisible()));
            }
            if (StringUtils.isNotBlank(menu.getStatus())){
                menuStream = menuStream.filter(m -> menu.getStatus().equals(m.getStatus()));
            }

            // 排序 - 先按parentId排序，再按orderNum排序
            List<SysMenu> filteredMenus = menuStream
                    .sorted(Comparator.comparing(SysMenu::getParentId)
                            .thenComparing(SysMenu::getOrderNum))
                    .collect(Collectors.toList());

            return BeanUtil.copyToList(filteredMenus, SysMenuVO.class);
        }
    }

}
