package site.ahzx.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.context.RequestHeaderContext;
import site.ahzx.domain.bo.LoginRequest;
import site.ahzx.except.RemoteServiceException;
import site.ahzx.service.LoginService;
import site.ahzx.config.JwtTokenUtil;
import util.R;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final LoginService loginService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtil;
    public AuthController(LoginService loginService, AuthenticationManager authenticationManager, JwtTokenUtil jwtUtil) {
        this.loginService = loginService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.info("login attempt for user: {}", loginRequest.getUsername());

        try {
            // 1. 获取原始请求的Header
            String tenantId  = request.getHeader("x-tenant-id");
            // 2. 组装新Header
            Map<String, String> headers = new HashMap<>();
            if (tenantId == null) {
                return R.fail("x-tenant-id is required");
            }
            headers.put("x-tenant-id", tenantId);
            RequestHeaderContext.setHeaders(headers);


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    );

            Authentication authentication = authenticationManager.authenticate(authenticationToken);


            String token = jwtUtil.generateToken(
                    loginRequest.getUsername(),
                    authentication.getAuthorities()
            );

            return R.ok("登录成功", token);

        } catch (RemoteServiceException e) {
            log.error("远程服务异常: {}", e.getMessage());
            return R.fail(e.getMessage()); // 返回具体的远程服务错误信息

        } catch (UsernameNotFoundException e) {
            log.warn("用户不存在: {}", loginRequest.getUsername());
            return R.fail("用户名或密码错误"); // 不透露具体是用户名错误

        } catch (BadCredentialsException e) {
            log.warn("密码错误: {}", loginRequest.getUsername());
            return R.fail("用户名或密码错误"); // 统一提示

        } catch (AuthenticationException e) {
            log.error("认证异常: {}", e.getMessage());
            return R.fail("认证失败，请稍后再试");

        } catch (Exception e) {
            log.error("系统异常: ", e);
            return R.fail("系统繁忙，请稍后再试");
        }
    }
}
