package site.ahzx.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import site.ahzx.context.RequestHeaderContext;
import site.ahzx.domain.bo.LoginRequest;
import site.ahzx.domain.vo.LoginVO;
import site.ahzx.except.RemoteServiceException;
import site.ahzx.service.LoginService;
import site.ahzx.utils.JwtTokenUtil;
import util.R;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.UUID.*;

@Slf4j
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtil;
    private final DefaultKaptcha captchaProducer;

    private final RedisTemplate<String, String> redisTemplate; // 用于存验证码

    private static final long CAPTCHA_EXPIRATION = 5 * 60; // 5分钟


    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtUtil, DefaultKaptcha captchaProducer, RedisTemplate<String, String> redisTemplate) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

        this.captchaProducer = captchaProducer;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.info("login attempt for user: {}", loginRequest.getUsername());
        /**
         * 先判断验证码正确再说
         */
        String captchaKey = loginRequest.getUuid();
        String captcha = loginRequest.getCode();
        String cachedCode = redisTemplate.opsForValue().get("captcha:"+captchaKey);
        if (cachedCode == null) {
            return R.fail("验证码已过期");
        }

        if (!cachedCode.equalsIgnoreCase(captcha)) {
            return R.fail("验证码错误");
        }

        // 删除已用验证码（防止重放）
        redisTemplate.delete(captchaKey);

        try {
            // 1. 表单中获取 x-tenant-id
            String tenantId  = loginRequest.getTenantId();
            // 2. 组装新Header
            Map<String, String> headers = new HashMap<>();
            if (tenantId == null) {
                return R.fail("tenant-id is required");
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
                    authentication.getAuthorities(),tenantId
            );

            LoginVO loginVO = new LoginVO();
            loginVO.setAccess_token(token);
            loginVO.setClientId(loginRequest.getClientId());

            return R.ok("登录成功", loginVO);

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

    @GetMapping("/code")
    public R<Map<String, Object>> getCode() throws IOException {
        String uuid = randomUUID().toString().replace("-", "");
        String code = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(code);

        // 保存验证码到 Redis
        redisTemplate.opsForValue().set("captcha:" + uuid, code, CAPTCHA_EXPIRATION, TimeUnit.SECONDS);

        // 图片转 base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        String base64Img = Base64.getEncoder().encodeToString(outputStream.toByteArray());

        Map<String, Object> data = new HashMap<>();
        data.put("captchaEnabled", true);
        data.put("uuid", uuid);
        data.put("img", base64Img);

        return R.ok(data);
    }
}
