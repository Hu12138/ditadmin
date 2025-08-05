package site.ahzx.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurity {
    private final SecurityUrlProperties securityUrlProperties;

    public SpringSecurity(SecurityUrlProperties securityUrlProperties) {
        this.securityUrlProperties = securityUrlProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ServerProperties serverProperties) throws Exception {

        http
                .formLogin(form -> form.disable()) // 禁用默认表单登录
                .httpBasic(httpBasic -> httpBasic.disable()) // 禁用默认httpBasic登录
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> {
                            // 只保留白名单配置，其他都去掉
                            if (securityUrlProperties.getWhitelist() != null) {
                                securityUrlProperties.getWhitelist().forEach(
                                        path -> auth.requestMatchers(path).permitAll()
                                );
                            }

                            // 所有其他请求都允许访问，权限控制交给 @PreAuthorize
                            auth.anyRequest().permitAll();
                        }
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("系统不支持登录");
        };
    }
}