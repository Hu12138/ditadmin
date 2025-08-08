package site.ahzx.service.impl;

import org.springframework.stereotype.Service;
import site.ahzx.domain.entity.SysConfig;
import site.ahzx.service.SysConfigService;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Override
    public String selectConfigByKey(String configKey) {

        return SysConfig.create().select(SysConfig::getConfigValue).where(SysConfig::getConfigKey).eq(configKey).one().getConfigValue();
    }
}
