package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
@Table("sys_roles")
public class SysRoles extends TenantBaseEntity<SysRoles> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 角色名称（可以中文）
     */
    private String roleName;
    /**
     * 角色编码（英文）
     */
    private String roleCode;
    /**
     * 角色描述
     */
    private String roleDesc;
    /**
     * 角色排序
     */
    private Integer roleOrder;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限 ）
     */
    private String dataScope;
    /**
     * 状态 （0正常 1停用）
     */
    private Integer status;

    @RelationManyToMany(
            joinTable = "sys_role_menus",
            selfField = "id", joinSelfColumn = "role_id",
            targetField = "id", joinTargetColumn = "menu_id"
    )
    private List<SysMenus> menus;

}
