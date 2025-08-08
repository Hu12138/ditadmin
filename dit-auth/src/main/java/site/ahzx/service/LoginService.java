package site.ahzx.service;

public interface LoginService {
    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    public String login(String username, String password);

    public void logout();
}
