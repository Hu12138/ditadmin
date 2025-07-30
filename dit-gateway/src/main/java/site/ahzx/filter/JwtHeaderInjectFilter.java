package site.ahzx.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
@Slf4j
public class JwtHeaderInjectFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> {
                    if (auth != null && auth.isAuthenticated()) {
                        String username = auth.getName();
                        List<String> roles = auth.getAuthorities().stream()
                                .map(a -> a.getAuthority().replace("ROLE_", ""))
                                .toList();

                        String tenantId = "";

                        Object details = auth.getDetails();
                        if (details instanceof java.util.Map<?, ?> map) {
                            tenantId = String.valueOf(map.get("tenantId"));
                        }

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("x-user-name", username)
                                .header("x-user-roles", String.join(",", roles))
                                .header("x-tenant-id", tenantId)
                                .build();

                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(mutatedExchange);
                    }
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        // 放在认证之后执行
        return SecurityWebFiltersOrder.AUTHENTICATION.getOrder() + 1;
    }
}