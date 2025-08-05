package site.ahzx.service;

import cn.hutool.core.lang.tree.Tree;
import site.ahzx.domain.bo.PageBO;

import java.util.List;

public interface DeptService {
    List<Tree<Long>> getDeptTree();
}
