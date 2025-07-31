package site.ahzx.mapper;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.ahzx.domain.entity.SysMenus;

import java.util.List;

@Mapper
public interface SysMenusMapper extends BaseMapper<SysMenus> {
    public List<SysMenus>  selectMenusByUserId(Long userId);
}
