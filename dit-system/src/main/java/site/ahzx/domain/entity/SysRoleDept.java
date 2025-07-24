package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Table("sys_role_depts")
@Data(staticConstructor = "create")
public class SysRoleDept extends Model<SysRoleDept> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long depId;

}
