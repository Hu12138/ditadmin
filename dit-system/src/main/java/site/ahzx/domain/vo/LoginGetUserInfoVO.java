package site.ahzx.domain.vo;

import lombok.Data;
import site.ahzx.domain.entity.SysUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class LoginGetUserInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private SysUser user;
    private List<String> roles;
    private List<String> permissions;
    public LoginGetUserInfoVO(){
        this.roles = new ArrayList<>();
        this.permissions = new ArrayList<>();
    }
}
