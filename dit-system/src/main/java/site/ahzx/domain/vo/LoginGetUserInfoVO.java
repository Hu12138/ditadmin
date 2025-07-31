package site.ahzx.domain.vo;

import lombok.Data;
import site.ahzx.domain.entity.SysUsers;

import java.util.ArrayList;
import java.util.List;

@Data
public class LoginGetUserInfoVO {
    private SysUsers user;
    private List<String> roles;
    private List<String> permissions;
    public LoginGetUserInfoVO(){
        this.roles = new ArrayList<>();
        this.permissions = new ArrayList<>();
    }
}
