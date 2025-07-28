package site.ahzx.service;

import site.ahzx.domain.vo.SysUserVO;
import site.ahzx.domain.entity.SysUsers;

public interface UserService {
    public SysUsers getUserByUsername(String username);

    public SysUserVO getUserDtoByUsername(String username);
}
