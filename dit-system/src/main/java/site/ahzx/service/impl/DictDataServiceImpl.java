package site.ahzx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysDictDataBO;
import site.ahzx.domain.bo.SysDictTypeBO;
import site.ahzx.domain.entity.SysDictData;
import site.ahzx.domain.entity.SysDictType;
import site.ahzx.domain.vo.SysDictDataVO;
import site.ahzx.domain.vo.SysDictTypeVO;
import site.ahzx.mapper.SysDictDataMapper;
import site.ahzx.mapper.SysDictTypeMapper;
import site.ahzx.service.DictDataService;
import site.ahzx.service.DictTypeService;
import site.ahzx.util.StringUtils;
import site.ahzx.util.TableDataInfo;

import java.util.List;

@Slf4j
@Service
public class DictDataServiceImpl implements DictDataService {
    private final SysDictDataMapper sysDictDataMapper;

    public DictDataServiceImpl(SysDictDataMapper sysDictDataMapper) {
        this.sysDictDataMapper = sysDictDataMapper;
    }


    @Override
    public TableDataInfo<SysDictDataVO> selectPageDictDataList(SysDictDataBO dictDataBO, PageBO pageBO) {
        QueryWrapper wrapper = QueryWrapper.create().from(SysDictData.class);
        log.debug("dictDataBO:{}", dictDataBO);
        if (ObjectUtil.isNotNull(dictDataBO.getDictLabel())) {
            wrapper.eq(SysDictData::getDictLabel, dictDataBO.getDictLabel());
        }

        // 分页查询
        Page<SysDictData> page = sysDictDataMapper.paginate(pageBO.getPageNum(), pageBO.getPageSize(), wrapper);

        // 转换为 VO 列表
        List<SysDictDataVO> voList = BeanUtil.copyToList(page.getRecords(), SysDictDataVO.class);

        // 构建分页对象
        Page<SysDictDataVO> voPage = new Page<>(page.getPageNumber(), page.getPageSize(), page.getTotalRow());
        voPage.setRecords(voList);
        return TableDataInfo.build(voPage);
    }
}
