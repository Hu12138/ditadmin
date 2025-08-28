package site.ahzx.service;

import site.ahzx.domain.bo.SysMenuBO;
import site.ahzx.domain.vo.RouterVO;
import site.ahzx.domain.vo.SysMenuVO;

import java.util.List;

public interface MenuService {

    public List<RouterVO> getRouterByUserId(Long userId);

    List<SysMenuVO> selectMenuList(SysMenuBO menu, Long userId);
}
