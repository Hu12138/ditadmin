package site.ahzx.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import org.springframework.stereotype.Service;
import site.ahzx.domain.entity.SysDepts;
import site.ahzx.service.DeptService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeptServiceImpl implements DeptService {
    @Override
    public List<Tree<Long>> getDeptTree() {
        List<SysDepts> sysDeptsList = SysDepts.create().list();

        // 先转换为 TreeNode 列表

        // 构造 TreeNode 列表
        List<TreeNode<Long>> nodes = sysDeptsList.stream()
                .map(dept -> {
                    TreeNode<Long> node = new TreeNode<>();
                    node.setId(dept.getId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getDeptName());
                    node.setWeight(Optional.ofNullable(dept.getOrderNum())
                            .map(Integer::parseInt).orElse(0));

                    Map<String, Object> extra = new HashMap<>();
                    extra.put("ancestors", dept.getAncestors());
                    extra.put("status", dept.getStatus());
                    extra.put("leader", dept.getLeader());
                    extra.put("label", dept.getDeptName());
                    node.setExtra(extra);
//                    node.setExtra(Map.of(
//                            "ancestors", dept.getAncestors(),
//                            "status", dept.getStatus(),
//                            "leader", dept.getLeader()
//                    ));
                    return node;
                })
                .toList();


        // 构建树，默认从 parentId = 0 开始
        return TreeUtil.build(nodes, 0L);
    }
}
