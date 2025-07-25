package site.ahzx.flex.config;

import com.mybatisflex.core.tenant.TenantFactory;
import site.ahzx.flex.context.LoginContext;

public class MyTenantFactory implements TenantFactory {
    @Override
    public Object[] getTenantIds() {
        Long tenantId = LoginContext.get().getTenantId();
        return new Object[]{tenantId};
    }

}
