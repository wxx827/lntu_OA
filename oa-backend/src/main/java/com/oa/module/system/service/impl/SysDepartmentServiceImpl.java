package com.oa.module.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.module.system.entity.SysDepartment;
import com.oa.module.system.mapper.SysDepartmentMapper;
import com.oa.module.system.service.SysDepartmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements SysDepartmentService {

    @Override
    public List<SysDepartment> getTree() {
        List<SysDepartment> allDeps = this.list();
        // Group by Parent ID (String)
        Map<String, List<SysDepartment>> parentMap = allDeps.stream()
                .collect(Collectors.groupingBy(d -> d.getParentId() == null ? "0" : d.getParentId()));
        
        // Root nodes are those with parentId = "0" or null (defaulted to "0" above)
        List<SysDepartment> roots = parentMap.get("0");
        if (roots == null) {
            roots = new ArrayList<>();
        }
        
        buildTree(roots, parentMap);
        return roots;
    }
    
    private void buildTree(List<SysDepartment> nodes, Map<String, List<SysDepartment>> parentMap) {
        for (SysDepartment node : nodes) {
            List<SysDepartment> children = parentMap.get(node.getDepId());
            if (children != null) {
                node.setChildren(children);
                buildTree(children, parentMap);
            }
        }
    }
}