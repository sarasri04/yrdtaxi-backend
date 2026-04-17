package com.yrdtaxi.service;


import com.yrdtaxi.model.*;
import com.yrdtaxi.repository.*;
import com.yrdtaxi.util.DistanceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepo;

    @Autowired
    private DriverLocationRepository locationRepo;
 
    @Autowired
    private RatingService ratingService;
    
    public Driver findNearestDriver(double userLat, double userLng) {

        List<DriverLocation> locations = locationRepo.findAll();

        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        for (DriverLocation loc : locations) {

            // ✅ Fix null pointer
            if (loc.getDriver() == null) continue;

            Driver driver = loc.getDriver();

            if (!driver.isAvailable()) continue;

            double distance = DistanceCalculator.calculate(
                    userLat, userLng,
                    loc.getLatitude(), loc.getLongitude()
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }

        return nearestDriver;
    }

    public Driver getDriverById(Long driverId) {
        return driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }
    
    public double getDriverRating(Long driverId) {
        return ratingService.getAverageRating(driverId);
    }
	
}


