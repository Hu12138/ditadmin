package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true) // 明确包含父类字段
@Table("sys_user")
public class SysUser extends TenantBaseEntity<SysUser> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 性别 0：女 1：男
     */
    private Integer sex;
    /**
     * 状态 （0正常 1停用）
     */
    private Integer status;
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @Column(isLogicDelete = true)
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    @RelationManyToMany(
            joinTable = "sys_user_roles",
            selfField = "user_id", joinSelfColumn = "user_id",
            targetField = "id", joinTargetColumn = "role_id"
    )
    private List<SysRole> roles;

    @RelationManyToMany(
            joinTable = "sys_user_depts",
            selfField = "id", joinSelfColumn = "user_id",
            targetField = "id", joinTargetColumn = "dept_id"
    )
    private List<SysDept> depts;

}
