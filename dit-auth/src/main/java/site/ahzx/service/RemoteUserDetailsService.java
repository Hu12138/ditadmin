package site.ahzx.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface RemoteUserDetailsService  extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
