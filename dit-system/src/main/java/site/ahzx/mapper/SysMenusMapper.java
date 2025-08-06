package site.ahzx.mapper;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.ahzx.domain.entity.SysMenu;

import java.util.List;

@Mapper
public interface SysMenusMapper extends BaseMapper<SysMenu> {
    public List<SysMenu>  selectMenusByUserId(Long userId);
}
