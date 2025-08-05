package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Table("sys_dict_type")
@Data(staticConstructor = "create")
public class SysDictType extends TenantBaseEntity<SysDictType> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id(keyType = KeyType.Auto)
    private Long id;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 状态：0正常，1停用
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

    @RelationOneToMany(selfField = "dictType", targetField = "dictType")
    private List<SysDictData> dataList;

}
