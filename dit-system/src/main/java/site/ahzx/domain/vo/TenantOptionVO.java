package site.ahzx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TenantOptionVO  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String tenantId;
    private String companyName;
    private String domain;
}
