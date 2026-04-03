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

    public Driver findNearestDriver(double userLat, double userLng) {

        List<DriverLocation> locations = locationRepo.findAll();

        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        for (DriverLocation loc : locations) {

            if (!loc.getDriver().isAvailable()) continue;

            double distance = DistanceCalculator.calculate(
                    userLat, userLng,
                    loc.getLatitude(), loc.getLongitude()
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = loc.getDriver();
            }
        }

        return nearestDriver;
    }

	public Driver getDriverById(Long driverId) {
		// TODO Auto-generated method stub
		return null;
	}
}


