package com.yrdtaxi.service;


import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Driver;
import com.yrdtaxi.model.DriverLocation;
import com.yrdtaxi.repository.BookingRepository;
import com.yrdtaxi.repository.DriverLocationRepository;
import com.yrdtaxi.repository.DriverRepository;
import com.yrdtaxi.util.DistanceCalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepo;
    
    @Autowired
    private BookingRepository bookingRepo;
    
    @Autowired
    private DriverLocationRepository locationRepo;

    @Autowired
    private RatingService ratingService;

    public Driver addDriver(Driver driver) {
        return driverRepo.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverRepo.findAll();
    }

    public Driver getDriverById(Long driverId) {
        return driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

    public Driver updateAvailability(Long id, boolean available) {
        Driver driver = getDriverById(id);
        driver.setAvailable(available);
        return driverRepo.save(driver);
    }

    public Driver findNearestDriver(double userLat, double userLng) {

        List<DriverLocation> locations = locationRepo.findAll();

        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        for (DriverLocation loc : locations) {

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

    public double getDriverRating(Long driverId) {
        return ratingService.getAverageRating(driverId);
    }
    
    public List<Booking> getDriverBookings(Long driverId) {

        Driver driver = getDriverById(driverId);

        return bookingRepo.findByDriver(driver);
    }
    
    public List<Booking> getCompletedTrips(Long driverId) {

        Driver driver = getDriverById(driverId);

        return bookingRepo.findByDriverAndStatus(
                driver,
                "COMPLETED"
        );
    }
    
    public double getDriverEarnings(Long driverId) {

        Driver driver = getDriverById(driverId);

        List<Booking> trips =
                bookingRepo.findByDriverAndStatus(
                        driver,
                        "COMPLETED"
                );

        return trips.stream()
                .mapToDouble(Booking::getTotalFare)
                .sum();
    }
    
    public Driver goOnline(Long id) {

        Driver driver = driverRepo.findById(id)
                .orElseThrow();

        driver.setOnline(true);
        driver.setLastSeen(LocalDateTime.now());

        return driverRepo.save(driver);
    }

    public Driver goOffline(Long id) {

        Driver driver = driverRepo.findById(id)
                .orElseThrow();

        driver.setOnline(false);

        return driverRepo.save(driver);
    }
}




