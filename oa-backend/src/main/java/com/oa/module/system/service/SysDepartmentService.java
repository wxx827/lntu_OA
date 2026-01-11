package com.oa.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.module.system.entity.SysDepartment;

import java.util.List;

public interface SysDepartmentService extends IService<SysDepartment> {
    List<SysDepartment> getTree();
}