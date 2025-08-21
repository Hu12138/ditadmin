package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Column;
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
@Table("sys_role")
public class SysRole extends TenantBaseEntity<SysRole> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long roleId;
    /**
     * 角色名称（可以中文）
     */
    private String roleName;
    /**
     * 角色编码（英文）
     */
    private String roleKey;

    private Integer roleSort;
    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限 ）
     */
    private String dataScope;


    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    private Boolean menuCheckStrictly;
    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    private Boolean deptCheckStrictly;
    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @Column(isLogicDelete = true)
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    @RelationManyToMany(
            joinTable = "sys_role_menu",
            selfField = "roleId", joinSelfColumn = "role_id",
            targetField = "menuId", joinTargetColumn = "menu_id"
    )
    private List<SysMenu> menus;

}
