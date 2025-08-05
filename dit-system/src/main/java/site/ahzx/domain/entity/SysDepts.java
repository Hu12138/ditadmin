package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Table("sys_depts")
@Data(staticConstructor = "create")
public class SysDepts extends BaseEntity<SysDepts> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 部门上级id
     */
    private Long parentId;
    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 祖级列表
     */
    private String ancestors;
    /**
     * 排序
     */
    private String orderNum;
    /**
     * 状态 （0正常 1停用）
     */
    private String status;
    /**
     * 负责人
     */
    private Long leader;


}
