package site.ahzx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.domain.vo.SysUserVO;
import site.ahzx.domain.entity.SysUser;
import site.ahzx.service.UserService;
import site.ahzx.util.R;

@RestController
@RequestMapping("/internal")
public class InternalController {
    private  final UserService userService ;
    public InternalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUserPassword")
    public R<?> getUserPassword(@RequestParam("username") String username) {
        SysUser user = userService.getUserByUsername(username);
        if (user == null) {
            return R.fail("用户不存在");
        }
        else {
            return R.ok("用户存在",user.getPassword());
        }
    }
    @GetMapping("getUserInfo")
    public R<?> getUserInfo(@RequestParam("username") String username) {
        SysUserVO user = userService.getUserDtoByUsername(username);
        if (user == null) {
            return R.fail("用户不存在");
        }
        else {
            return R.ok("用户存在",user);
        }
    }
}
