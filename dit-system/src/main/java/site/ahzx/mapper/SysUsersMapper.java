package site.ahzx.mapper;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import site.ahzx.domain.entity.SysUser;

@Mapper
public interface SysUsersMapper extends BaseMapper<SysUser> {
}
