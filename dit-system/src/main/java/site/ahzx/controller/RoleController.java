package site.ahzx.controller;

import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysRoleBO;
import site.ahzx.domain.entity.SysRole;
import site.ahzx.domain.vo.SysRoleVO;
import site.ahzx.service.RoleService;
import site.ahzx.util.TableDataInfo;

import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public TableDataInfo<SysRoleVO> list(SysRoleBO sysRoleBO, PageBO pageBO){

        return roleService.selectRoleList(sysRoleBO, pageBO);
    }


}
