package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Table("sys_role_departments")
public class SysRoleDept implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long departmentId;

}
