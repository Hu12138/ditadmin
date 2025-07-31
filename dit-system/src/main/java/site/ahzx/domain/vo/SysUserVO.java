package site.ahzx.domain.vo;

import lombok.Data;

import java.util.List;


@Data
public class SysUserVO {
    private Long tenantId;
    private Long userId;
    private String username;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
