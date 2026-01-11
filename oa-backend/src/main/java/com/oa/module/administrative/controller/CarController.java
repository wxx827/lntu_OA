package com.oa.module.administrative.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.oa.common.Result;
import com.oa.module.administrative.entity.OaCar;
import com.oa.module.administrative.mapper.OaCarMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/car")
@Tag(name = "车辆管理", description = "Vehicle Management API")
public class CarController {

    @Autowired
    private OaCarMapper carMapper;

    @GetMapping("/list")
    @Operation(summary = "获取车辆列表")
    public Result<List<OaCar>> getList(@RequestParam(required = false) Integer state) {
        try {
            LambdaQueryWrapper<OaCar> query = new LambdaQueryWrapper<>();
            if (state != null) {
                query.eq(OaCar::getState, state);
            }
            query.orderByDesc(OaCar::getCreateTime);
            return Result.success(carMapper.selectList(query));
        } catch (Exception e) {
            return Result.error("获取车辆列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取车辆详情")
    public Result<OaCar> getById(@PathVariable String id) {
        try {
            OaCar car = carMapper.selectById(id);
            if (car == null) {
                return Result.error(404, "车辆不存在");
            }
            return Result.success(car);
        } catch (Exception e) {
            return Result.error("获取车辆详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    @Operation(summary = "添加车辆")
    public Result<String> add(@RequestBody OaCar car) {
        try {
            // 验证必填字段
            if (car.getCarLicence() == null || car.getCarLicence().trim().isEmpty()) {
                return Result.error("车牌号不能为空");
            }
            // 检查车牌号是否已存在
            LambdaQueryWrapper<OaCar> query = new LambdaQueryWrapper<>();
            query.eq(OaCar::getCarLicence, car.getCarLicence().trim());
            if (carMapper.selectCount(query) > 0) {
                return Result.error("车牌号已存在");
            }
            
            car.setCreateTime(new Date());
            if (car.getState() == null) {
                car.setState(0); // 默认空闲
            }
            if (car.getMileage() == null) {
                car.setMileage(0);
            }
            if (car.getSeats() == null) {
                car.setSeats(5);
            }
            carMapper.insert(car);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error("添加车辆失败: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "更新车辆信息")
    public Result<String> update(@PathVariable String id, @RequestBody OaCar car) {
        try {
            OaCar existing = carMapper.selectById(id);
            if (existing == null) {
                return Result.error(404, "车辆不存在");
            }
            // 如果修改了车牌号，检查是否重复
            if (car.getCarLicence() != null && !car.getCarLicence().equals(existing.getCarLicence())) {
                LambdaQueryWrapper<OaCar> query = new LambdaQueryWrapper<>();
                query.eq(OaCar::getCarLicence, car.getCarLicence().trim());
                query.ne(OaCar::getCarId, id);
                if (carMapper.selectCount(query) > 0) {
                    return Result.error("车牌号已存在");
                }
            }
            car.setCarId(id);
            carMapper.updateById(car);
            return Result.success("更新成功");
        } catch (Exception e) {
            return Result.error("更新车辆失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除车辆")
    public Result<String> delete(@PathVariable String id) {
        try {
            OaCar car = carMapper.selectById(id);
            if (car == null) {
                return Result.error(404, "车辆不存在");
            }
            if (car.getState() != null && car.getState() == 1) {
                return Result.error("车辆使用中，无法删除");
            }
            carMapper.deleteById(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除车辆失败: " + e.getMessage());
        }
    }
    
    @PutMapping("/state/{id}")
    @Operation(summary = "更新车辆状态")
    public Result<String> updateState(@PathVariable String id, @RequestParam Integer state) {
        try {
            OaCar car = carMapper.selectById(id);
            if (car == null) {
                return Result.error(404, "车辆不存在");
            }
            if (state < 0 || state > 2) {
                return Result.error("无效的状态值");
            }
            car.setState(state);
            carMapper.updateById(car);
            String stateText = state == 0 ? "空闲" : (state == 1 ? "使用中" : "维护中");
            return Result.success("状态已更新为: " + stateText);
        } catch (Exception e) {
            return Result.error("更新状态失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/stats")
    @Operation(summary = "获取车辆统计")
    public Result<Map<String, Object>> getStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", carMapper.selectCount(null));
            stats.put("free", carMapper.selectCount(new LambdaQueryWrapper<OaCar>().eq(OaCar::getState, 0)));
            stats.put("busy", carMapper.selectCount(new LambdaQueryWrapper<OaCar>().eq(OaCar::getState, 1)));
            stats.put("maintenance", carMapper.selectCount(new LambdaQueryWrapper<OaCar>().eq(OaCar::getState, 2)));
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取统计失败: " + e.getMessage());
        }
    }
}