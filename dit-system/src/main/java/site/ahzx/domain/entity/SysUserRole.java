package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data(staticConstructor = "create")
@Table("sys_user_role")
public class SysUserRole extends Model<SysUserRole> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long roleId;
}
