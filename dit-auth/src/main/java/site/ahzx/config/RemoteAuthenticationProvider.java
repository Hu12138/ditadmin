package site.ahzx.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.ahzx.service.RemoteUserDetailsService;

@Component
public class RemoteAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService remoteUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public RemoteAuthenticationProvider(UserDetailsService remoteUserDetailsService,
                                        PasswordEncoder passwordEncoder) {
        this.remoteUserDetailsService = remoteUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();
        UserDetails userDetails = remoteUserDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, rawPassword, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
