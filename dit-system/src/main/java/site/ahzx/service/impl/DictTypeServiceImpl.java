package site.ahzx.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import site.ahzx.domain.entity.SysDictData;
import site.ahzx.domain.entity.SysDictType;
import site.ahzx.domain.vo.SysDictDataVO;
import site.ahzx.mapper.SysDictTypeMapper;
import site.ahzx.service.DictTypeService;

import java.util.List;

@Service
public class DictTypeServiceImpl implements DictTypeService {
    private final SysDictTypeMapper dictTypeMapper;

    public DictTypeServiceImpl(SysDictTypeMapper dictTypeMapper) {
        this.dictTypeMapper = dictTypeMapper;
    }

    @Override
    public List<SysDictDataVO> getDictDataByType(String dictType) {

      return   dictTypeMapper.selectListWithRelationsByQueryAs(
              QueryWrapper.create().where(SysDictData::getDictType).eq(dictType),
              SysDictDataVO.class
      );
    }
}
