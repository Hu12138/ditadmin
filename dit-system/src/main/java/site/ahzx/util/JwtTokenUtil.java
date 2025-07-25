package site.ahzx.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class JwtTokenUtil {;
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-seconds}")
    private long expirationHours;

    private Key key;  // 使用安全的Key类型替代原始字符串
    // 初始化密钥
    @PostConstruct //这个注解的作用是在所有bean初始化之后执行该方法
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成Token
     * JWT 本身不是加密，只是编码，不要放敏感信息如密码
     * @param username 用户名
     * @param roles 角色
     * @return token
     * ├─ 构建Header
     * │     └─ 包含算法(如HS256)、令牌类型(JWT)
     * ├─ 构建Payload
     * │     ├─ 标准声明(过期时间、签发时间、用户名)
     * │     └─ 自定义声明(角色列表roles)
     * └─ 生成Signature
     *       └─ 用密钥对Header+Payload签名
     */
    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {

        List<? extends GrantedAuthority> safeRoles =
                new ArrayList<>(Optional.ofNullable(roles).orElse(Collections.emptyList()));
        if ( safeRoles.isEmpty()) {
            log.warn("No roles found for user {}", username);
        }
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", safeRoles.
                stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationHours))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            JwtParser parser = Jwts.parser().verifyWith((SecretKey) key).build();
            parser.parseSignedClaims(token);
            return true;
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
        return false;
    }


}
