package com.oa.mapper;

import com.oa.entity.Car;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * Car Mapper Interface
 */
public interface CarMapper {
    
    /**
     * Get all cars
     */
    List<Car> selectAll();
    
    /**
     * Get car by ID
     */
    Car selectById(@Param("carId") Long carId);
    
    /**
     * Insert new car
     */
    int insert(Car car);
    
    /**
     * Update car
     */
    int update(Car car);
    
    /**
     * Delete car by ID
     */
    int deleteById(@Param("carId") Long carId);
    
    /**
     * Get statistics by status
     */
    Map<String, Integer> selectStatsByStatus();
    
    /**
     * Count total cars
     */
    int countTotal();
}