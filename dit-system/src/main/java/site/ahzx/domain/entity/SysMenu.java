package site.ahzx.domain.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import site.ahzx.constant.Constants;
import site.ahzx.constant.SystemConstants;
import site.ahzx.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 考虑了一下，菜单还是不用继承tenant了，因为菜单是公共的，不区分租户
 */
@Table("sys_menu")
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity<SysMenu> implements Serializable {
    /**
     * 序列化ID
     */
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    private Long menuId;
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
     * 是否为外链（0是 1否）
     */
    private String isFrame;
    /**
     * 是否缓存（0 缓存 1不缓存）
     */
    private String isCache;
    /**
     * 图标
     */
    private String icon;
    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;
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


    /**
     * 子菜单
     */
    @Column(ignore = true)
    private List<SysMenu> children = new ArrayList<>();

    public void addChild(SysMenu child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }
}
