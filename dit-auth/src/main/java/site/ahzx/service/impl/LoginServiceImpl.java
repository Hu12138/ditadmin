package site.ahzx.service.impl;
import org.springframework.stereotype.Service;
import site.ahzx.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
    @Override
    public String login(String username, String password){
        //TODO 检查用户是否存在
        // TODO 检查密码是否正确
        // TODO 生成token

        return "token";
    }

    @Override
    public void logout() {

    }
}
