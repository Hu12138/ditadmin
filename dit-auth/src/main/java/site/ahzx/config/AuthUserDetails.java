package site.ahzx.config;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class AuthUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Long tenantId;
    private final Long userId;
//    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

}
