package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data(staticConstructor = "create")
@Table("sys_user_depts")
public class SysUserDepts extends Model<SysUserDepts> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 部门ID
     */
    private Long deptId;
}
