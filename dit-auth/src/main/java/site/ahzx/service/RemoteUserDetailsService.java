package site.ahzx.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface RemoteUserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
