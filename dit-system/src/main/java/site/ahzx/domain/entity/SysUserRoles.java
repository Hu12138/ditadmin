package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Table(name = "sys_user_roles")
public class SysUserRoles implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long roleId;
}
