package com.oa.entity;

import lombok.Data;
import java.util.Date;

/**
 * Car Entity - Maps to OA_CAR table
 */
@Data
public class Car {
    private Long carId;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer seats;
    private String status; // AVAILABLE, IN_USE, MAINTENANCE
    private Integer mileage;
    private Date lastMaintenanceDate;
    private Date createTime;
    private Date updateTime;
}