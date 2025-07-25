package site.ahzx.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.ahzx.domain.entity.SysUsers;
import site.ahzx.service.UserService;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public SysUsers getUserByUsername(String username) {
        log.info("username: {}", username);
//        return SysUsers.create().where("username = ?", username).one();
        return SysUsers.create().where(SysUsers::getUsername).eq(username).one();
    }
}
