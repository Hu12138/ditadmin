package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_roles")
public class SysRoles extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
     * 状态 0：禁用 1：正常
     */
    private Integer status;

}
