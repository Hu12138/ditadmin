package site.ahzx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class TenantLoginVO  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 是否开启租户功能
     */
    private Boolean tenantEnabled;

    private List<TenantOptionVO> voList;
}


