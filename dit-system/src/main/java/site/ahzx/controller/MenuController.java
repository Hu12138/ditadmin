package site.ahzx.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.domain.bo.SysMenuBO;
import site.ahzx.domain.vo.RouterVO;
import site.ahzx.domain.vo.SysMenuVO;
import site.ahzx.flex.context.LoginContext;
import site.ahzx.service.MenuService;
import site.ahzx.util.R;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("getRouters")
    public R<?> getRouters(HttpServletRequest request){
        /*
         * 从header中提取用户id
         */
        String userId = request.getHeader("x-user-id");
        // 用户名查角色，角色查菜单
        List<RouterVO> routerVOList = menuService.getRouterByUserId(Long.valueOf(userId));
        return R.ok(routerVOList);

    }

    @GetMapping("/list")
    public R<List<SysMenuVO>> list(SysMenuBO menu) {

        List<SysMenuVO> menus = menuService.selectMenuList(menu, LoginContext.get().getUserId());
        return R.ok(menus);
    }
}
