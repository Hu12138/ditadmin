package site.ahzx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.domain.entity.SysTenant;
import site.ahzx.domain.vo.TenantLoginVO;
import site.ahzx.domain.vo.TenantOptionVO;
import site.ahzx.service.TenantService;
import site.ahzx.util.R;
import cn.hutool.core.bean.BeanUtil;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/tenant")
public class TenantController {
    private final TenantService tenantService;
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }
    @GetMapping("/list")
    public R<?> list() {
        List<SysTenant> tenantList = tenantService.getTenantList();
        //TODO: 租户关闭情况
        /**
         * 暂时写死，打开租户功能
         */
        Boolean openTenant = true;
        TenantLoginVO tenantLoginVO = new TenantLoginVO();
        tenantLoginVO.setTenantEnabled(openTenant);
        List<TenantOptionVO> voList = tenantList.stream()
                .map(tenant -> BeanUtil.copyProperties(tenant, TenantOptionVO.class))
                .toList();

        tenantLoginVO.setVoList(voList);

        return R.ok("success",tenantLoginVO);
    }
}
