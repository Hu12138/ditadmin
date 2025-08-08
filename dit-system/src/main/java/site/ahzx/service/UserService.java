package site.ahzx.service;

import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysUserBO;
import site.ahzx.domain.vo.LoginGetUserInfoVO;
import site.ahzx.domain.vo.SysUserInfoVO;
import site.ahzx.domain.vo.SysUserNoPassVO;
import site.ahzx.domain.vo.SysUserVO;
import site.ahzx.domain.entity.SysUser;
import site.ahzx.util.TableDataInfo;

public interface UserService {
    public SysUser getUserByUsername(String username);

    /**
     * 通过用户名获取用户信息,给auth返回的，内部服务调用
     */
    public SysUserVO getUserDtoByUsername(String username);

    /**
     * 通过用户名获取用户信息,给登录接口返回的
     */
    public LoginGetUserInfoVO getLoginUserInfo(String username);

    public TableDataInfo<SysUserNoPassVO> getUserList(PageBO pageBO);

    /**
     * 检查用户是否存在
     */
    public Boolean checkUserExist(String username);

    /**
     * 检查手机号是否存在
     */
    public Boolean checkPhoneExist(String phone);

    /**
     * 检查邮箱是否存在
     */
    public Boolean checkEmailExist(String email);

    Integer addUser(SysUserBO userBO);

    /**
     * 根据用户id查找用户信息
     */
    public SysUserInfoVO getUserNoPassById(Long userId );

    int resetUserPwd(   Long userId, String password);
}
