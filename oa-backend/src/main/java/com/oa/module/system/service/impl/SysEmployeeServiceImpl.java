package com.oa.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oa.module.system.entity.SysEmployee;
import com.oa.module.system.mapper.SysEmployeeMapper;
import com.oa.module.system.service.SysEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class SysEmployeeServiceImpl extends ServiceImpl<SysEmployeeMapper, SysEmployee> implements SysEmployeeService {
    @Override
    public SysEmployee getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysEmployee>().eq(SysEmployee::getEmpName, username));
    }
}