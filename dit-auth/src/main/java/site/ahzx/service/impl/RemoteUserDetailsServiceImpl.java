package site.ahzx.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.ahzx.config.AuthUserDetails;
import site.ahzx.context.RequestHeaderContext;
import site.ahzx.domain.dto.SysUserDTO;
import site.ahzx.feign.RemoteUserFeign;
import site.ahzx.service.RemoteUserDetailsService;
import site.ahzx.utils.R;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RemoteUserDetailsServiceImpl implements RemoteUserDetailsService {
    private final RemoteUserFeign remoteUserFeign;
    private final ObjectMapper objectMapper;
    public RemoteUserDetailsServiceImpl(RemoteUserFeign remoteUserFeign, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.remoteUserFeign = remoteUserFeign;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("loadUserByUsername: {}", username);
        var data = remoteUserFeign.getUserInfo(username).getData();

        log.debug("feign get data: {}", data);
        SysUserDTO sysUser = objectMapper.convertValue(data, SysUserDTO.class);
        R<?> result;
        try {
            result = remoteUserFeign.getUserInfo(username);
        } catch (Exception e) {
            log.error("调用远程用户服务失败", e);
            throw new UsernameNotFoundException("用户服务不可用");
        }
        log.info("result: {}", result);

        if (result == null || result.getCode() != 200 || result.getData() == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("x-user-id", String.valueOf(sysUser.getUserId()));
        RequestHeaderContext.setHeaders(headers);

        // 组装权限信息（角色 + 权限）
        List<SimpleGrantedAuthority> authorities = sysUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        if (sysUser.getPermissions() != null) {
            authorities.addAll(sysUser.getPermissions().stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList());
        }

        return new AuthUserDetails(
                sysUser.getUserName(),
                sysUser.getPassword(),
                sysUser.getTenantId(),
                sysUser.getUserId(),
                authorities
        );
    }
}
