package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Table("sys_depts")
@Data(staticConstructor = "create")
public class SysDept extends TenantBaseEntity<SysDept> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long deptId;
    /**
     * 部门上级id
     */
    private Long parentId;
    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门类别编码
     */
    private String deptCategory;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private Long leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态:0正常,1停用
     */
    private String status;


    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @Column(isLogicDelete = true)
    private String delFlag;

    /**
     * 祖级列表
     */
    private String ancestors;



}
