package site.ahzx.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public String login() {
        log.info("login");
        return "redirect:/";
    }

}
