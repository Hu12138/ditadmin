package site.ahzx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ahzx.domain.bo.PageBO;
import site.ahzx.domain.bo.SysDictTypeBO;
import site.ahzx.domain.vo.SysDictTypeVO;
import site.ahzx.service.DictTypeService;
import site.ahzx.util.TableDataInfo;

@RestController
@RequestMapping("/dict/type")
public class DictTypeController {

    private final DictTypeService dictTypeService;

    public DictTypeController(DictTypeService dictTypeService) {
        this.dictTypeService = dictTypeService;
    }

    /**
     * 查询字典类型列表
     */
    @GetMapping("/list")
    public TableDataInfo<SysDictTypeVO> list(SysDictTypeBO dictType, PageBO pageQuery) {
        return dictTypeService.selectPageDictTypeList(dictType, pageQuery);
    }
}
