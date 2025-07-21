package site.ahzx.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import site.ahzx.service.RemoteUserDetailsService;

public class RemoteUserDetailsServiceImpl implements RemoteUserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return null;
    }
}
