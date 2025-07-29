package site.ahzx.domain.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String scope;
    private String openid;
    private String access_token;
    private String refresh_token;
    private String expires_in;
    private String refresh_expire_in;
    private String clientId;
}
