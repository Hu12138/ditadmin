package site.ahzx.controller;

import org.springframework.web.bind.annotation.*;
import site.ahzx.domain.entity.SysDepts;
import site.ahzx.domain.entity.SysUsers;
import site.ahzx.service.UserService;
import site.ahzx.util.R;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/test")
    public R<?> test() {
        return R.ok("Hello, world");
    }




}
