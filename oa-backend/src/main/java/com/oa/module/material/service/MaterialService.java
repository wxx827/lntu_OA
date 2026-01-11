package com.oa.module.material.service;

import com.oa.module.material.entity.OaMatApply;
import com.oa.module.material.entity.OaMaterial;

import java.util.List;

public interface MaterialService {
    List<OaMaterial> getList();
    
    void apply(OaMatApply apply);
    
    List<OaMatApply> getMyApply(String empId);
    
    void approve(String applyId);
    void decreaseStock(String matId, Integer count);
}