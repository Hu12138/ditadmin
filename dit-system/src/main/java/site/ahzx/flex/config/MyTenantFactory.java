package site.ahzx.flex.config;

import com.mybatisflex.core.tenant.TenantFactory;
import site.ahzx.flex.context.LoginContext;

public class MyTenantFactory implements TenantFactory {
    @Override
    public Object[] getTenantIds() {

        String tenantId = LoginContext.get().getTenantId();
        return new Object[]{tenantId};
    }

}
