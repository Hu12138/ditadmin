package site.ahzx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import site.ahzx.domain.entity.SysMenus;
import site.ahzx.domain.vo.MetaVO;
import site.ahzx.domain.vo.RouterVO;
import site.ahzx.flex.context.LoginContext;
import site.ahzx.mapper.SysMenusMapper;
import site.ahzx.service.MenuService;
import site.ahzx.constant.RedisCachePrefix;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SysMenusMapper sysMenuMapper; // 假设你有这个 mapper 从数据库查菜单

    @Override
    public List<RouterVO> getRouterByUserId(Long userId) {
        try {
            String key = RedisCachePrefix.USER_MENUS_PREFIX + LoginContext.get().getTenantId() + ":" + userId;
            @SuppressWarnings("unchecked")
            List<SysMenus> cachedMenus = (List<SysMenus>) redisTemplate.opsForValue().get(key);

            List<SysMenus> menus;
            if (cachedMenus != null && !cachedMenus.isEmpty()) {
                menus = cachedMenus;
            } else {
                // TODO: 从数据库查询用户权限菜单
                menus = sysMenuMapper.selectMenusByUserId(userId);
                // 缓存
                redisTemplate.opsForValue().set(key, menus, Duration.ofHours(3));
            }

            // 构建 RouterVO 树结构
            List<RouterVO> routerList = buildRouterTree(menus);


            return routerList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把 SysMenus 列表转换为 RouterVO 树结构
     */
    private List<RouterVO> buildRouterTree(List<SysMenus> menuList) {
        Map<Long, RouterVO> idToRouter = new HashMap<>();
        List<RouterVO> rootRouters = new ArrayList<>();

        for (SysMenus menu : menuList) {
            if ("F".equals(menu.getMenuType())) continue; // 过滤按钮类型

            RouterVO router = new RouterVO();
            router.setName(menu.getRouteName());
            router.setPath(menu.getPath());
            router.setComponent(menu.getComponent());
            router.setQuery(menu.getQueryParam());
            router.setHidden("1".equals(menu.getVisible()));
            router.setAlwaysShow(false); // 可根据需要调整
            router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), menu.getIsCache() == 1));

            idToRouter.put(menu.getId(), router);

            if (menu.getParentId() == 0) {
                rootRouters.add(router);
            } else {
                RouterVO parent = idToRouter.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(router);
                }
            }
        }

        return rootRouters;
    }
}
