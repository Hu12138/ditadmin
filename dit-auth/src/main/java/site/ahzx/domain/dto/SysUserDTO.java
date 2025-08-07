package site.ahzx.domain.dto;

import lombok.Data;

import java.util.List;


@Data
public class SysUserDTO {
    private Long tenantId;
    private Long userId;
    private String userName;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
