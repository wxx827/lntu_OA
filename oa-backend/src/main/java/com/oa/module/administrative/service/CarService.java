package com.oa.module.administrative.service;

import com.oa.entity.Car;
import java.util.List;
import java.util.Map;

/**
 * Car Service Interface
 */
public interface CarService {
    
    /**
     * Get all cars
     */
    List<Car> getAllCars();
    
    /**
     * Get car by ID
     */
    Car getCarById(Long carId);
    
    /**
     * Add new car
     */
    Long addCar(Car car);
    
    /**
     * Update car
     */
    boolean updateCar(Car car);
    
    /**
     * Delete car
     */
    boolean deleteCar(Long carId);
    
    /**
     * Get car statistics
     */
    Map<String, Object> getCarStats();
}