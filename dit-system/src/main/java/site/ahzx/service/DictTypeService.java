package site.ahzx.service;

import site.ahzx.domain.vo.SysDictDataVO;

import java.util.List;

public interface DictTypeService {
    public List<SysDictDataVO> getDictDataByType(String dictType);
}
