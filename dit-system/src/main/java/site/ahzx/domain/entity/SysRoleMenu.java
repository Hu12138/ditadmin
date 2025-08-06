package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data(staticConstructor = "create")
@Table("sys_role_menu")
public class SysRoleMenu extends Model<SysRoleMenu> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单ID
     */
    private Long menuId;
}
