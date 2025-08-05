package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Table("sys_depts")
@Data
public class TenantBaseEntity<T extends BaseEntity<T>> extends BaseEntity<T> {
    /**
     * 租户id
     */
    @Column(tenantId = true)
    private String tenantId;
}
