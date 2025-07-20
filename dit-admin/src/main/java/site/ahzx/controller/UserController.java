package site.ahzx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.R;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/test")
    public R<?> test() {
        return R.ok("Hello, world");
    }
}
