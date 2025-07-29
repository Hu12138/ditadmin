package site.ahzx.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class TenantLoginVO {
    /**
     * 是否开启租户功能
     */
    private Boolean tenantEnabled;

    private List<TenantOptionVO> voList;
}


