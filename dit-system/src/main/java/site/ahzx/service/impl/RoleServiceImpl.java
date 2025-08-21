package site.ahzx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysRoleBO;
import site.ahzx.domain.entity.SysRole;
import site.ahzx.domain.vo.SysRoleVO;
import site.ahzx.mapper.SysRoleMapper;
import site.ahzx.service.RoleService;
import site.ahzx.util.StringUtils;
import site.ahzx.util.TableDataInfo;

import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final SysRoleMapper sysRoleMapper;

    public RoleServiceImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }


    private QueryWrapper buildQueryWrapper(SysRoleBO sysRoleBO) {
        QueryWrapper wrapper = QueryWrapper.create().from(SysRole.class);
        if (ObjectUtil.isNotNull(sysRoleBO.getRoleId())){
            wrapper.eq("role_id", sysRoleBO.getRoleId());
        }
        if (StringUtils.isNotBlank(sysRoleBO.getRoleName()) )
        {
            wrapper.like("role_name", sysRoleBO.getRoleName());
        }
        if (StringUtils.isNotBlank(sysRoleBO.getStatus())) {
            wrapper.eq(SysRole::getStatus, sysRoleBO.getStatus());
        }

        if (StringUtils.isNotBlank(sysRoleBO.getRoleKey())) {
            wrapper.like(SysRole::getRoleKey, sysRoleBO.getRoleKey());
        }

        log.debug("wrapper sql is :{}", wrapper.toSQL());
        return wrapper;


    }
    @Override
    public TableDataInfo<SysRoleVO> selectRoleList(SysRoleBO sysRoleBO, PageBO pageBO) {
        QueryWrapper queryWrapper = this.buildQueryWrapper(sysRoleBO);
        Page<SysRole> page = sysRoleMapper.paginate(pageBO.getPageNum(), pageBO.getPageSize(), queryWrapper);


        List<SysRoleVO> voList = BeanUtil.copyToList(page.getRecords(), SysRoleVO.class);

        Page<SysRoleVO> voPage = new Page<>(page.getPageNumber(), page.getPageSize(), page.getTotalRow());
        voPage.setRecords(voList);

        return TableDataInfo.build(voPage);
    }
}
