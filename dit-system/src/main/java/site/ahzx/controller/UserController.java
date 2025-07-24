package site.ahzx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.domain.entity.SysDepts;
import site.ahzx.domain.entity.SysUsers;
import util.R;

@RestController
@RequestMapping("/system")
public class UserController {
    @GetMapping("/test")
    public R<?> test() {
        return R.ok("Hello, world");
    }

    @GetMapping("/user")
    public R<?> user(SysDepts depts) {
        SysUsers user =  SysUsers.create();

        return R.ok("Hello, world2");
    }
}
