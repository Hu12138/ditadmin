package site.ahzx.service;

import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.vo.LoginGetUserInfoVO;
import site.ahzx.domain.vo.RouterVO;
import site.ahzx.domain.vo.SysUserNoPassVO;
import site.ahzx.domain.vo.SysUserVO;
import site.ahzx.domain.entity.SysUsers;
import site.ahzx.util.R;
import site.ahzx.util.TableDataInfo;

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

    public TableDataInfo<SysUserNoPassVO> getUserList(PageBO pageBO);
}
