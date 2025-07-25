package site.ahzx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.service.TenantService;
import site.ahzx.util.R;

@RestController
@RequestMapping("/tenant")
public class TenantController {
    private final TenantService tenantService;
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }
    @GetMapping("/list")
    public R<?> list() {
        return R.ok("success",tenantService.getTenantList());
    }
}
