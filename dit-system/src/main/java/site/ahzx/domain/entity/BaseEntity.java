package site.ahzx.domain.entity;


import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

//@EqualsAndHashCode(callSuper = false)
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BaseEntity <T extends BaseEntity<T>> extends Model<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 创建者
     */
    private String  createdBy;
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    /**
     * 更新者
     */
    private String updatedBy;

    /**
     * 租户ID
     */
    @Column(tenantId = true)
    private String tenantId;
}
