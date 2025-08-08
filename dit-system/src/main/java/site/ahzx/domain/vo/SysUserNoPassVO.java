package site.ahzx.domain.vo;

import lombok.Data;
import site.ahzx.domain.entity.SysDept;
import site.ahzx.domain.entity.SysRole;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysUserNoPassVO  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    /**
     * 用户名
     */
    private String userName;

    private Long deptId;

    private String deptName;

    private String userType;
    /**
     * 昵称
     */
    private String nickName;
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
    private String sex;
    /**
     * 状态 （0正常 1停用）
     */
    private String status;

    private String loginIp;

    private String remark;


    private LocalDateTime createTime;

     // 对应 SysUser 中的 private SysDept depts;

    /**
     * 角色对象
     */
    private List<SysRoleVO> roles;

    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;

    /**
     * 数据权限 当前角色ID
     */
    private Long roleId;
}
