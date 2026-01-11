package com.oa.module.administrative.service.impl;

import com.oa.entity.Car;
import com.oa.mapper.CarMapper;
import com.oa.module.administrative.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Car Service Implementation
 */
@Service
@Transactional
public class CarServiceImpl implements CarService {

    @Autowired
    private CarMapper carMapper;

    @Override
    public List<Car> getAllCars() {
        return carMapper.selectAll();
    }

    @Override
    public Car getCarById(Long carId) {
        return carMapper.selectById(carId);
    }

    @Override
    public Long addCar(Car car) {
        if (car.getStatus() == null) {
            car.setStatus("AVAILABLE");
        }
        carMapper.insert(car);
        return car.getCarId();
    }

    @Override
    public boolean updateCar(Car car) {
        return carMapper.update(car) > 0;
    }

    @Override
    public boolean deleteCar(Long carId) {
        return carMapper.deleteById(carId) > 0;
    }

    @Override
    public Map<String, Object> getCarStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Get total count
        int total = carMapper.countTotal();
        stats.put("total", total);
        
        // Get status breakdown - returns a single Map, not a List
        Map<String, Integer> statusMap = carMapper.selectStatsByStatus();
        
        int available = statusMap.getOrDefault("AVAILABLE", 0);
        int inUse = statusMap.getOrDefault("IN_USE", 0);
        int maintenance = statusMap.getOrDefault("MAINTENANCE", 0);
        
        stats.put("available", available);
        stats.put("inUse", inUse);
        stats.put("maintenance", maintenance);
        
        return stats;
    }
}