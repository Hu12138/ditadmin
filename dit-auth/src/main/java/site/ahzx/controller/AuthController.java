package site.ahzx.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ahzx.domain.bo.LoginRequest;
import site.ahzx.service.LoginService;
import util.R;

@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {
    private  final LoginService loginService;
    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("login");
        String token = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return R.ok("login success",token);
    }

}
