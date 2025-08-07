package site.ahzx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
public class SysUserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long tenantId;
    private Long userId;
    private String userName;
    private String password;
    private List<String> roles;
    private List<String> permissions;
}
