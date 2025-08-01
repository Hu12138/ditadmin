package site.ahzx.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenUtil {;
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-seconds}")
    private long expirationSeconds;

    private Key key;  // 使用安全的Key类型替代原始字符串
    // 初始化密钥
    @PostConstruct //这个注解的作用是在所有bean初始化之后执行该方法
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }






    public Claims parseToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build();

            Jws<Claims> jwsClaims = parser.parseSignedClaims(token);
            return jwsClaims.getPayload(); // 返回解析出的 Claims
        } catch (ExpiredJwtException ex) {
            // Token已过期
            log.info("Token expired", ex);
        } catch (SecurityException ex) {
            // 签名验证失败
            log.info("Security exception", ex);
        } catch (Exception e) {
            // 其他无效Token情况
            log.info("Other exception", e);
        }
        return null;
    }


}
