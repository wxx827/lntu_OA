package com.oa.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oa.module.system.entity.SysEmployee;

public interface SysEmployeeService extends IService<SysEmployee> {
    SysEmployee getByUsername(String username);
}