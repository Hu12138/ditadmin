package site.ahzx.service;

import site.ahzx.domain.vo.RouterVO;

import java.util.List;

public interface MenuService {

    public List<RouterVO> getRouterByUserId(Long userId);
}
