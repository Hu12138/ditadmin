package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Table("sys_menus")
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysMenus extends BaseEntity<SysMenus> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 请求参数
     */
    private String queryParam;
    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 是否为外链（0是 1否）
     */
    private int isFrame;
    /**
     * 是否缓存（0 缓存 1不缓存）
     */
    private int isCache;
    /**
     * 图标
     */
    private String icon;
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String   menuType;
    /**
     * 显示状态（0显示 1隐藏）
     */
    private String visible;
    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;
    /**
     * 权限标识
     */
    private String perms;
    /**
     * 备注
     */
    private String remark;

}
