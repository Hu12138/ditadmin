package site.ahzx.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;
import site.ahzx.filter.JwtAuthenticationConverter;
import site.ahzx.filter.NotServerWebExchangeMatcher;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebFluxSecurityConfig.class);
    private final SecurityUrlProperties securityUrlProperties;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public WebFluxSecurityConfig(SecurityUrlProperties securityUrlProperties, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.securityUrlProperties = securityUrlProperties;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        AuthenticationWebFilter authenticationWebFilter =
                new AuthenticationWebFilter(jwtReactiveAuthenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);
        authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        List<String> whitelist = securityUrlProperties.getWhitelist();
        ServerWebExchangeMatcher whitelistMatcher = ServerWebExchangeMatchers.pathMatchers(whitelist.toArray(new String[0]));

        // 非白名单路径才执行过滤器逻辑
        ServerWebExchangeMatcher nonWhitelistMatcher = new NotServerWebExchangeMatcher(whitelistMatcher);
        authenticationWebFilter.setRequiresAuthenticationMatcher(nonWhitelistMatcher);


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
                }).addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION); ;

        return http.build();
    }
    @Bean
    public ReactiveAuthenticationManager jwtReactiveAuthenticationManager() {
        // 此 manager 的作用是表示 token 已通过 JwtAuthenticationConverter 转换并信任，无需额外校验
        return Mono::just;
    }
}