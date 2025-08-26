package site.ahzx.controller;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysDictDataBO;
import site.ahzx.domain.vo.SysDictDataVO;
import site.ahzx.service.DictDataService;
import site.ahzx.service.DictTypeService;
import site.ahzx.util.R;
import site.ahzx.util.TableDataInfo;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dict/data")
public class DictDataController {
    private final DictTypeService dictTypeService;
    private final DictDataService dictDataService;

    public DictDataController(DictTypeService dictTypeService, DictDataService dictDataService) {
        this.dictTypeService = dictTypeService;
        this.dictDataService = dictDataService;
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     */
    @GetMapping(value = "/type/{dictType}")
    public R<List<SysDictDataVO>> dictType(@PathVariable String dictType) {
        List<SysDictDataVO> data = dictTypeService.getDictDataByType(dictType);

        return R.ok(data);
    }

    /**
     * 查询字典数据列表
     */

    @GetMapping("/list")
    public TableDataInfo<SysDictDataVO> list(SysDictDataBO dictDataBO, PageBO pageBO) {

        return dictDataService.selectPageDictDataList(dictDataBO, pageBO);
    }
}
