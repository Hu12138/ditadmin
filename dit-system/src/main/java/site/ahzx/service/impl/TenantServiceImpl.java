package site.ahzx.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.ahzx.domain.entity.SysTenant;
import site.ahzx.mapper.SysTenantMapper;
import site.ahzx.service.TenantService;

import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private SysTenantMapper tenantMapper;

    @Override
    public List<SysTenant> getTenantList() {

       return TenantManager.withoutTenantCondition(tenantMapper::selectAll);
//       return SysTenant.create().list();
//        return tenantMapper.selectAll();
    }
}
