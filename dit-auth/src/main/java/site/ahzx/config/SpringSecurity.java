package site.ahzx.config;


import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import site.ahzx.service.RemoteUserDetailsService;

@Configuration
public class SpringSecurity {
    private final SecurityUrlProperties securityUrlProperties;
    private final RemoteUserDetailsService remoteUserDetailsService;

    public SpringSecurity(SecurityUrlProperties securityUrlProperties, RemoteUserDetailsService remoteUserDetailsService) {
        this.securityUrlProperties = securityUrlProperties;
        this.remoteUserDetailsService = remoteUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RemoteAuthenticationProvider remoteAuthenticationProvider) throws Exception {

        http
                .authenticationProvider(remoteAuthenticationProvider)
                .formLogin(AbstractHttpConfigurer::disable) // 禁用默认表单登录
                .csrf(AbstractHttpConfigurer::disable)
//                .csrf(x -> x.disable())
                .authorizeHttpRequests(
                auth -> {
                    // 动态注册白名单
                    if (securityUrlProperties.getWhitelist() != null) {
                        securityUrlProperties.getWhitelist().forEach(
                                path  -> auth.requestMatchers(path).permitAll()
                        );
                    }
                    // 动态注册黑名单
                    if (securityUrlProperties.getBlacklist() != null) {
                        securityUrlProperties.getBlacklist().forEach(
                                path  -> auth.requestMatchers(path).denyAll()
                        );
                    }
                    // 动态注册角色映射
                    if (securityUrlProperties.getRoleMappings() != null) {
                        securityUrlProperties.getRoleMappings().forEach(
                               rm -> auth.requestMatchers(rm.getPattern()).hasRole(rm.getRole()
                        ));
                    }
                    // 其他请求需认证
                    auth.anyRequest().authenticated();
                }
        );
        return http.build();
    }

    // 提供 PasswordEncoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService( remoteUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
