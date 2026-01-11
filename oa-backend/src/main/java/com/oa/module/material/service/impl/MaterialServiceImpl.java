package com.oa.module.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.exception.BusinessException;
import com.oa.module.material.entity.OaMatApply;
import com.oa.module.material.entity.OaMaterial;
import com.oa.module.material.mapper.OaMatApplyMapper;
import com.oa.module.material.mapper.OaMaterialMapper;
import com.oa.module.material.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private OaMaterialMapper materialMapper;
    @Autowired
    private OaMatApplyMapper applyMapper;

    @Override
    public List<OaMaterial> getList() {
        return materialMapper.selectList(null);
    }

    @Override
    public void apply(OaMatApply apply) {
        OaMaterial material = materialMapper.selectById(apply.getMatId());
        if (material == null) {
            throw new BusinessException("Material not found");
        }
        if (material.getStock() < apply.getCount()) {
            throw new BusinessException("Stock insufficient");
        }
        
        apply.setState(0); // Pending
        apply.setApplyDate(new Date());
        applyMapper.insert(apply);
    }

    @Override
    public List<OaMatApply> getMyApply(String empId) {
        List<OaMatApply> list = applyMapper.selectList(new LambdaQueryWrapper<OaMatApply>()
                .eq(OaMatApply::getEmpId, empId)
                .orderByDesc(OaMatApply::getApplyDate));
        // 填充物资名称
        for (OaMatApply apply : list) {
            if (apply.getMatId() != null) {
                OaMaterial mat = materialMapper.selectById(apply.getMatId());
                if (mat != null) {
                    apply.setMatName(mat.getMatName());
                }
            }
        }
        return list;
    }

    @Override
    @Transactional
    public void approve(String applyId) {
        OaMatApply apply = applyMapper.selectById(applyId);
        if (apply == null) return;
        
        if (apply.getState() == 1) return; // Already approved
        
        // Deduct stock
        decreaseStock(apply.getMatId(), apply.getCount());
        
        apply.setState(1);
        applyMapper.updateById(apply);
    }

    @Override
    public void decreaseStock(String matId, Integer count) {
        int rows = materialMapper.deductStock(matId, count);
        if (rows == 0) {
            throw new BusinessException("Stock insufficient for approval");
        }
    }
}