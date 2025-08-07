package site.ahzx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysUserNoPassVO  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    /**
     * 用户名
     */
    private String userName;

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
    private Integer sex;
    /**
     * 状态 （0正常 1停用）
     */
    private Integer status;

    private LocalDateTime createdAt;
}
