package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_departments")
public class SysDepartments extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 部门上级id
     */
    private Long parentId;
    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 祖级列表
     */
    private String ancestors;
    /**
     * 排序
     */
    private String orderNum;
    /**
     * 状态 0：禁用 1：正常
     */
    private String status;
    /**
     * 负责人
     */
    private Long leader;


}
