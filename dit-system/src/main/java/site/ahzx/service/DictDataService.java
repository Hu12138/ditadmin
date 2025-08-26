package site.ahzx.service;

import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysDictDataBO;
import site.ahzx.domain.bo.SysDictTypeBO;
import site.ahzx.domain.vo.SysDictDataVO;
import site.ahzx.domain.vo.SysDictTypeVO;
import site.ahzx.util.TableDataInfo;

import java.util.List;

public interface DictDataService {


    TableDataInfo<SysDictDataVO> selectPageDictDataList(SysDictDataBO dictDataBO, PageBO pageBO);

}
