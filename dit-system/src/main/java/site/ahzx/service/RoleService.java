package site.ahzx.service;

import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysRoleBO;
import site.ahzx.domain.vo.SysRoleVO;
import site.ahzx.util.TableDataInfo;

public interface RoleService {
    public TableDataInfo<SysRoleVO> selectRoleList(SysRoleBO sysRoleBO, PageBO pageBO);
}
