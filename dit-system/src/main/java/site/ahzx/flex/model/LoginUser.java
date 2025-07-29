package site.ahzx.flex.model;

import lombok.Data;

import java.util.Set;

@Data
public class LoginUser {
    private Long userId;
    private String username;
    private Long deptId;
    private Set<Long> customDeptIds; // dataScope=2 时使用
    private String dataScope; // 值为 1~5
    private String tenantId;


}
