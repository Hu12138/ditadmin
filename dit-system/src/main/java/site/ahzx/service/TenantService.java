package site.ahzx.service;

import org.springframework.stereotype.Service;
import site.ahzx.domain.entity.SysTenant;

import java.util.List;


public interface TenantService {

    public List<SysTenant> getTenantList();
}
