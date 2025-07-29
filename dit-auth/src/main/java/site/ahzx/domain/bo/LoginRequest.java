package site.ahzx.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String clientId;
    private String tenantId;
    private String username;
    private String password;
    private String grantType;
    private String uuid;
    private String code;
//    private String loginType;
}
