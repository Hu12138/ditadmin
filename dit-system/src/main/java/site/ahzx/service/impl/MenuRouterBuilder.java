package site.ahzx.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import site.ahzx.constant.Constants;
import site.ahzx.constant.SystemConstants;
import site.ahzx.domain.entity.SysMenu;
import site.ahzx.domain.vo.MetaVO;
import site.ahzx.domain.vo.RouterVO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MenuRouterBuilder {

    public static List<RouterVO> buildMenus(List<SysMenu> menus) {
        // 先构建完整的菜单树（包含父子关系）
        List<SysMenu> menuTree = buildMenuTree(menus);
        // 然后从根节点开始构建路由
        return buildRouterTree(menuTree);
    }

    /**
     * 将扁平列表转换为树形结构
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
        return rootMenus;
    }
    /**
     * 递归构建路由树
     */
    private static List<RouterVO> buildRouterTree(List<SysMenu> menus) {
        List<RouterVO> routers = new LinkedList<>();
        for (SysMenu menu : menus) {
            RouterVO router = convertToRouter(menu);

            // 处理子菜单（所有类型菜单都可能包含子项）
            if (CollUtil.isNotEmpty(menu.getChildren())) {
                router.setChildren(buildRouterTree(menu.getChildren()));

                // 目录类型特殊处理
                if (SystemConstants.TYPE_DIR.equals(menu.getMenuType())) {
                    router.setAlwaysShow(true);
                    router.setRedirect("noRedirect");
                }
            }
            routers.add(router);
        }
        return routers;
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
    /**
     * 生成路由名称
     */
    private static String generateRouteName(SysMenu menu) {
        String baseName = StringUtils.capitalize(menu.getPath());
        // 一级菜单且是框架菜单时名称置空
        if (menu.getParentId() == 0 && isMenuFrame(menu)) {
            return "";
        }
        return (StringUtils.isNotBlank(baseName) ? baseName : "route") + menu.getMenuId();
    }

    /**
     * 获取路由路径
     */
    private static String getRouterPath(SysMenu menu) {
        // 内链特殊处理
        if (menu.getParentId() != 0 && isInnerLink(menu)) {
            return innerLinkReplaceEach(menu.getPath());
        }

        // 一级目录
        if (menu.getParentId() == 0) {
            if (SystemConstants.TYPE_DIR.equals(menu.getMenuType()) &&
                    SystemConstants.NO_FRAME.equals(menu.getIsFrame())) {
                return "/" + menu.getPath();
            }
            // 一级菜单框架
            if (isMenuFrame(menu)) {
                return "/";
            }
        }
        return menu.getPath();
    }

    /**
     * 获取组件信息
     */
    private static String getComponentInfo(SysMenu menu) {
        // 非框架菜单直接返回组件路径
        if (!isMenuFrame(menu) && StringUtils.isNotBlank(menu.getComponent())) {
            return menu.getComponent();
        }

        // 内链特殊处理
        if (menu.getParentId() != 0 && isInnerLink(menu)) {
            return SystemConstants.INNER_LINK;
        }

        // 父视图处理
        if (menu.getParentId() != 0 && SystemConstants.TYPE_DIR.equals(menu.getMenuType())) {
            return SystemConstants.PARENT_VIEW;
        }

        // 默认布局组件
        return SystemConstants.LAYOUT;
    }

    /**
     * 处理菜单框架
     */
    private static void handleMenuFrame(RouterVO parent, SysMenu menu) {
        parent.setMeta(null); // 清除父级meta

        RouterVO child = new RouterVO();
        child.setName(StringUtils.capitalize(menu.getPath()) + menu.getMenuId());
        child.setPath(menu.getPath());
        child.setComponent( menu.getComponent());
        child.setQuery(menu.getQueryParam());
        child.setMeta(new MetaVO(
                menu.getMenuName(),
                menu.getIcon(),
                "1".equals(menu.getIsCache()),
                menu.getPath()
        ));

        parent.setChildren(List.of(child));
    }

    /**
     * 处理内链
     */
    private static void handleInnerLink(RouterVO parent, SysMenu menu) {
        parent.setPath("/inner_link/" + menu.getMenuId()); // 确保路径唯一
        parent.setComponent(SystemConstants.LAYOUT);
        parent.setMeta(new MetaVO(
                menu.getMenuName(),
                menu.getIcon(),
                "1".equals(menu.getIsCache()),
                menu.getPath()
        ));

        RouterVO child = new RouterVO();
        child.setPath(innerLinkReplaceEach(menu.getPath()));
        child.setComponent(SystemConstants.INNER_LINK);
        child.setName("InnerLink" + menu.getMenuId());
        child.setMeta(new MetaVO(
                menu.getMenuName(),
                menu.getIcon(),
                menu.getPath()
        ));

        parent.setChildren(List.of(child));
    }

    /**
     * 判断是否是菜单框架
     */
    private static boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId() == 0 &&
                SystemConstants.TYPE_MENU.equals(menu.getMenuType()) &&
                SystemConstants.NO_FRAME.equals(menu.getIsFrame());
    }

    /**
     * 判断是否是内链
     */
    private static boolean isInnerLink(SysMenu menu) {
        return SystemConstants.NO_FRAME.equals(menu.getIsFrame()) &&
                StringUtils.startsWithAny(menu.getPath(), "http://", "https://");
    }

    /**
     * 内链地址转换
     */
    private static String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path,
                new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }
}