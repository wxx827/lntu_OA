package com.oa.module.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oa.module.material.entity.OaMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OaMaterialMapper extends BaseMapper<OaMaterial> {
    @Update("UPDATE OA_MATERIAL SET STOCK = STOCK - #{count} WHERE MAT_ID = #{matId} AND STOCK >= #{count}")
    int deductStock(String matId, Integer count);
}