package site.ahzx.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import site.ahzx.utils.JwtTokenUtil;

import java.util.*;

@Component
@Slf4j
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
    private final JwtTokenUtil jwtTokenUtil ;
    public JwtAuthenticationConverter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {


            log.info("in JwtAuthenticationConverter");
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // TODO: 校验 token、解析用户信息、角色等
                Claims claims = jwtTokenUtil.parseToken(token);
                log.info("JWT token: {}", claims.toString());
// 标准字段
                String subject = claims.getSubject();           // sub，username
                Date expiration = claims.getExpiration();       // exp，过期时间
                Date issuedAt = claims.getIssuedAt();           // iat，签发时间
                String issuer = claims.getIssuer();             // iss，签发者
                Set<String> audience = claims.getAudience();// aud，接收方

                List<String> roles = claims.get("roles", List.class);
                String tenantId = claims.get("tenantId",String.class);
                String username = claims.get("username",String.class);
                Long userId = claims.get("userId",Long.class);
                // 构建认证对象
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, token,
                                roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList());
                auth.setDetails(Map.of("tenantId", tenantId,"userId", userId));
                return Mono.just(auth);
            } else {
                log.info("JWT token is empty or error");
                return Mono.empty();
            }

    }
}
