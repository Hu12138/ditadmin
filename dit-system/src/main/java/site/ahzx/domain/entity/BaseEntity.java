package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Id;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
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
    private Long createdBy;
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    /**
     * 更新者
     */
    private Long updatedBy;
}
