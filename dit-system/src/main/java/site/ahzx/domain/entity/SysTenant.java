package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Table("sys_tenant")
@Data(staticConstructor = "create")
public class SysTenant extends TenantBaseEntity<SysTenant> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 联系人
     */
    private String contactUserName;
    /**
     * 联系人电话
     */
    private String contactPhone;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 统一社会信用代码
     */
    private String licenseNumber;
    /**
     * 地址
     */
    private String address;
    /**
     * 企业简介
     */
    private String intro;
    /**
     * 域名
     */
    private String domain;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态 （0正常 1停用）
     */
    private String status;
}
