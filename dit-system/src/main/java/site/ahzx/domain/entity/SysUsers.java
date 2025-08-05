package site.ahzx.domain.entity;

import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true) // 明确包含父类字段
@Table("sys_users")
public class SysUsers extends BaseEntity<SysUsers> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;
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
    private String phone;
    /**
     * 性别 0：女 1：男
     */
    private Integer sex;
    /**
     * 状态 （0正常 1停用）
     */
    private Integer status;

    @RelationManyToMany(
            joinTable = "sys_user_roles",
            selfField = "id", joinSelfColumn = "user_id",
            targetField = "id", joinTargetColumn = "role_id"
    )
    private List<SysRoles> roles;

    @RelationManyToMany(
            joinTable = "sys_user_depts",
            selfField = "id", joinSelfColumn = "user_id",
            targetField = "id", joinTargetColumn = "dept_id"
    )
    private List<SysDepts> depts;

}
