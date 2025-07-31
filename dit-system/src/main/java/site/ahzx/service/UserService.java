package site.ahzx.service;

import site.ahzx.domain.vo.LoginGetUserInfoVO;
import site.ahzx.domain.vo.SysUserVO;
import site.ahzx.domain.entity.SysUsers;

public interface UserService {
    public SysUsers getUserByUsername(String username);

    /**
     * 通过用户名获取用户信息,给auth返回的，内部服务调用
     */
    public SysUserVO getUserDtoByUsername(String username);

    /**
     * 通过用户名获取用户信息,给登录接口返回的
     */
    public LoginGetUserInfoVO getLoginUserInfo(String username);
}
