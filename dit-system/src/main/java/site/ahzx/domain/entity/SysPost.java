package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
@Table("sys_post")
public class SysPost extends TenantBaseEntity<SysPost> {

    /**
     * 岗位序号
     */
    @Id
    private Long postId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位类别编码
     */
    private String postCategory;

    /**
     * 岗位排序
     */
    private Integer postSort;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
