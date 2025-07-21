package site.ahzx.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebFluxSecurityConfig.class);
    private final SecurityUrlProperties securityUrlProperties;

    public WebFluxSecurityConfig(SecurityUrlProperties securityUrlProperties) {
        this.securityUrlProperties = securityUrlProperties;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(auth -> {
                    log.info("securityUrlProperties: {}", securityUrlProperties);
                    // 动态白名单
                    if (securityUrlProperties.getWhitelist() != null) {
                        securityUrlProperties.getWhitelist().forEach(p ->
                                auth.pathMatchers(p).permitAll());
                    }
                    // 动态黑名单
                    if (securityUrlProperties.getBlacklist() != null) {
                        securityUrlProperties.getBlacklist().forEach(p ->
                                auth.pathMatchers(p).denyAll());
                    }
                    // 动态角色映射
                    if (securityUrlProperties.getRoleMappings() != null) {
                        securityUrlProperties.getRoleMappings().forEach(rm ->
                                auth.pathMatchers(rm.getPattern()).hasRole(rm.getRole()));
                    }

                    // 默认其他全部认证
                    auth.anyExchange().authenticated();
                });

        return http.build();
    }
}