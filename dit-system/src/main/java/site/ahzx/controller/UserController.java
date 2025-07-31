package site.ahzx.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.ahzx.domain.entity.SysDepts;
import site.ahzx.domain.entity.SysUsers;
import site.ahzx.domain.vo.LoginGetUserInfoVO;
import site.ahzx.service.UserService;
import site.ahzx.util.R;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/test")
    public R<?> test() {
        return R.ok("Hello, world");
    }

    @GetMapping("getInfo")
    public R<?> getInfo(HttpServletRequest request) {
        String username = request.getHeader("x-user-name");
        log.debug("in getInfo username: {}", username);
        LoginGetUserInfoVO loginUserInfo = userService.getLoginUserInfo(username);
        return R.ok("获取成功", loginUserInfo);
    }




}
