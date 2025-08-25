package site.ahzx.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import site.ahzx.constant.Constants;
import site.ahzx.constant.SystemConstants;
import site.ahzx.domain.entity.SysMenu;
import site.ahzx.domain.vo.MetaVO;
import site.ahzx.domain.vo.RouterVO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuRouterBuilder {

    public static List<RouterVO> buildMenus(List<SysMenu> menus) {
        // 先构建完整的菜单树（包含父子关系，并排序）
        List<SysMenu> menuTree = buildMenuTree(menus);
        // 然后从根节点开始构建路由（并对子菜单排序）
        return buildRouterTree(menuTree);
    }

    /**
     * 将扁平列表转换为树形结构并排序
     */
    private static List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        Map<Long, SysMenu> menuMap = menus.stream()
                .collect(Collectors.toMap(SysMenu::getMenuId, Function.identity()));

        List<SysMenu> rootMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (SystemConstants.TYPE_BUTTON.equals(menu.getMenuType())) {
                continue; // 跳过按钮类型
            }

            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0) {
                rootMenus.add(menu);
            } else {
                SysMenu parent = menuMap.get(parentId);
                if (parent != null) {
                    parent.addChild(menu);
                }
            }
        }

        // 对整个根菜单树进行排序
        sortMenus(rootMenus);
        return rootMenus;
    }

    /**
     * 递归构建路由树
     */
    private static List<RouterVO> buildRouterTree(List<SysMenu> menus) {
        List<RouterVO> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVO router = convertToRouter(menu);

            // 处理子菜单
            if (CollUtil.isNotEmpty(menu.getChildren())) {
                // 子菜单排序
                sortMenus(menu.getChildren());

                router.setChildren(buildRouterTree(menu.getChildren()));

                // 目录类型特殊处理
                if (SystemConstants.TYPE_DIR.equals(menu.getMenuType())) {
                    router.setAlwaysShow(true);
                    router.setRedirect("noRedirect");
                }
            }
            routers.add(router);
        }

        // 当前层级排序
        routers.sort(Comparator.comparingInt(r -> Optional.ofNullable(r.getId()).map(id ->
                menus.stream().filter(m -> m.getMenuId().equals(id))
                        .findFirst().map(SysMenu::getOrderNum).orElse(0)
        ).orElse(0)));
        return routers;
    }

    /**
     * 菜单排序：orderNum升序
     */
    private static void sortMenus(List<SysMenu> menus) {
        menus.sort(Comparator.comparingInt(m -> Optional.ofNullable(m.getOrderNum()).orElse(0)));
        // 递归对子菜单也排序
        for (SysMenu menu : menus) {
            if (CollUtil.isNotEmpty(menu.getChildren())) {
                sortMenus(menu.getChildren());
            }
        }
    }

    /**
     * 将SysMenu转换为RouterVO
     */
    private static RouterVO convertToRouter(SysMenu menu) {
        RouterVO router = new RouterVO();
        router.setId(menu.getMenuId());
        router.setHidden("1".equals(menu.getVisible()));
        router.setName(generateRouteName(menu));
        router.setPath(getRouterPath(menu));
        router.setComponent(getComponentInfo(menu));
        router.setQuery(menu.getQueryParam());
        router.setMeta(new MetaVO(
                menu.getMenuName(),
                menu.getIcon(),
                "1".equals(menu.getIsCache()),
                menu.getPath()
        ));
        return router;
    }

    private static String generateRouteName(SysMenu menu) {
        String baseName = StringUtils.capitalize(menu.getPath());
        if (menu.getParentId() == 0 && isMenuFrame(menu)) {
            return "";
        }
        return (StringUtils.isNotBlank(baseName) ? baseName : "route") + menu.getMenuId();
    }

    private static String getRouterPath(SysMenu menu) {
        if (menu.getParentId() != 0 && isInnerLink(menu)) {
            return innerLinkReplaceEach(menu.getPath());
        }
        if (menu.getParentId() == 0) {
            if (SystemConstants.TYPE_DIR.equals(menu.getMenuType()) &&
                    SystemConstants.NO_FRAME.equals(menu.getIsFrame())) {
                return "/" + menu.getPath();
            }
            if (isMenuFrame(menu)) {
                return "/";
            }
        }
        return menu.getPath();
    }

    private static String getComponentInfo(SysMenu menu) {
        if (!isMenuFrame(menu) && StringUtils.isNotBlank(menu.getComponent())) {
            return menu.getComponent();
        }
        if (menu.getParentId() != 0 && isInnerLink(menu)) {
            return SystemConstants.INNER_LINK;
        }
        if (menu.getParentId() != 0 && SystemConstants.TYPE_DIR.equals(menu.getMenuType())) {
            return SystemConstants.PARENT_VIEW;
        }
        return SystemConstants.LAYOUT;
    }

    private static boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId() == 0 &&
                SystemConstants.TYPE_MENU.equals(menu.getMenuType()) &&
                SystemConstants.NO_FRAME.equals(menu.getIsFrame());
    }

    private static boolean isInnerLink(SysMenu menu) {
        return SystemConstants.NO_FRAME.equals(menu.getIsFrame()) &&
                StringUtils.startsWithAny(menu.getPath(), "http://", "https://");
    }

    private static String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path,
                new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }
}