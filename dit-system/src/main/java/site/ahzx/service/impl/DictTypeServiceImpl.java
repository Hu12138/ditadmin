package site.ahzx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysDictTypeBO;
import site.ahzx.domain.entity.SysDictData;
import site.ahzx.domain.entity.SysDictType;
import site.ahzx.domain.vo.SysDictDataVO;
import site.ahzx.domain.vo.SysDictTypeVO;
import site.ahzx.mapper.SysDictDataMapper;
import site.ahzx.mapper.SysDictTypeMapper;
import site.ahzx.service.DictTypeService;
import site.ahzx.util.StringUtils;
import site.ahzx.util.TableDataInfo;

import java.util.List;

@Service
public class DictTypeServiceImpl implements DictTypeService {
    private final SysDictDataMapper sysDictDataMapper;
    private final SysDictTypeMapper sysDictTypeMapper;
    public DictTypeServiceImpl(SysDictDataMapper sysDictDataMapper, SysDictTypeMapper sysDictTypeMapper) {
        this.sysDictDataMapper = sysDictDataMapper;
        this.sysDictTypeMapper = sysDictTypeMapper;
    }

    @Override
    public List<SysDictDataVO> getDictDataByType(String dictType) {

      return   sysDictDataMapper.selectListWithRelationsByQueryAs(
              QueryWrapper.create().where(SysDictData::getDictType).eq(dictType),
              SysDictDataVO.class
      );
    }

    @Override
    public TableDataInfo<SysDictTypeVO> selectPageDictTypeList(SysDictTypeBO dictTypeBO, PageBO pageQuery) {


        QueryWrapper wrapper = QueryWrapper.create().from(SysDictType.class);
        if (ObjectUtil.isNotNull(dictTypeBO.getDictId())) {
            wrapper.eq("dict_id", dictTypeBO.getDictId());
        }
        if (StringUtils.isNotBlank(dictTypeBO.getDictName())) {
            wrapper.like("dict_name", dictTypeBO.getDictName());
        }
        if (StringUtils.isNotBlank(dictTypeBO.getDictType())) {
            wrapper.eq(SysDictType::getDictType, dictTypeBO.getDictType());
        }
        // 分页查询
        Page<SysDictType> page = sysDictTypeMapper.paginate(pageQuery.getPageNum(), pageQuery.getPageSize(), wrapper);

        // 转换为 VO 列表
        List<SysDictTypeVO> voList = BeanUtil.copyToList(page.getRecords(), SysDictTypeVO.class);

        // 构建分页对象
        Page<SysDictTypeVO> voPage = new Page<>(page.getPageNumber(), page.getPageSize(), page.getTotalRow());
        voPage.setRecords(voList);

        return TableDataInfo.build(voPage);

    }
}
