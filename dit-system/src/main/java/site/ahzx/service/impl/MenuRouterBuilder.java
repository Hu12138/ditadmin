package site.ahzx.service.impl;
import org.apache.commons.lang3.StringUtils;
import site.ahzx.domain.entity.SysMenu;
import site.ahzx.domain.vo.MetaVO;
import site.ahzx.domain.vo.RouterVO;

import java.util.*;
import java.util.stream.Collectors;

public class MenuRouterBuilder {

    /**
     * 构建菜单树路由结构
     * @param menus 所有菜单项
     * @return 路由树
     */
    public static List<RouterVO> buildMenuTree(List<SysMenu> menus) {
        // 1. 过滤掉按钮类型（menuType = F）
        List<SysMenu> menuList = menus.stream()
                .filter(m -> !"F".equals(m.getMenuType()))
                .sorted(Comparator.comparing(SysMenu::getOrderNum, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());

        // 2. 构建 id -> SysMenus 映射
        Map<Long, RouterVO> routerMap = new LinkedHashMap<>();

        // 3. 创建 RouterVO 并填充 meta、基本字段
        for (SysMenu menu : menuList) {
            RouterVO router = new RouterVO();
            router.setId(menu.getId());
            router.setName(StringUtils.defaultIfBlank(menu.getRouteName(), menu.getMenuName()));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setHidden("1".equals(menu.getVisible()));
            router.setQuery(menu.getQueryParam());
            router.setMeta(new MetaVO(menu.getMenuName(), menu.getIcon(), "0".equals(menu.getIsCache() + ""), menu.getPerms()));

            routerMap.put(menu.getId(), router);
        }

        // 4. 构建树结构
        List<RouterVO> rootRouters = new ArrayList<>();
        for (SysMenu menu : menuList) {
            RouterVO router = routerMap.get(menu.getId());
            Long parentId = menu.getParentId();
            if (parentId == 0) {
                rootRouters.add(router);
            } else {
                RouterVO parentRouter = routerMap.get(parentId);
                if (parentRouter != null) {
                    if (parentRouter.getChildren() == null) {
                        parentRouter.setChildren(new ArrayList<>());
                    }
                    parentRouter.getChildren().add(router);
                    parentRouter.setAlwaysShow(true);
                }
            }
        }

        return rootRouters;
    }

    /**
     * 获取组件路径
     */
    private static String getComponent(SysMenu menu) {
        if (menu.getParentId() == 0 && "M".equals(menu.getMenuType()) && menu.getIsFrame() == 1) {
            return "Layout";
        } else if ("M".equals(menu.getMenuType())) {
            return StringUtils.defaultIfBlank(menu.getComponent(), "ParentView");
        } else if ("C".equals(menu.getMenuType())) {
            return menu.getComponent();
        }
        return null;
    }

    /**
     * 获取前端 path 字段
     */
    private static String getRouterPath(SysMenu menu) {
        String path = menu.getPath();
        if (menu.getParentId() == 0 && "M".equals(menu.getMenuType()) && menu.getIsFrame() == 1) {
            return "/" + path;
        }
        return path;
    }
}