package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Table("sys_depts")
@Data
@Accessors(chain = true)
public class TenantBaseEntity<T extends BaseEntity<T>> extends BaseEntity<T> {
    /**
     * 租户id
     */
    @Column(tenantId = true)
    private String tenantId;
}
