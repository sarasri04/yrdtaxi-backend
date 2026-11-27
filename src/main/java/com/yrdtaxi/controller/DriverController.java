package com.yrdtaxi.controller;

import com.yrdtaxi.dto.DriverLocationDTO;
import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Driver;
import com.yrdtaxi.model.DriverLocation;
import com.yrdtaxi.repository.DriverLocationRepository;
import com.yrdtaxi.service.DriverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverLocationRepository locationRepo;

    @PostMapping
    public Driver addDriver(@RequestBody Driver driver) {
        return driverService.addDriver(driver);
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{id}")
    public Driver getDriver(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}/availability")
    public Driver updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available
    ) {
        return driverService.updateAvailability(id, available);
    }

    @PostMapping("/location")
    public DriverLocation updateLocation(@RequestBody DriverLocationDTO dto) {

        Driver driver = driverService.getDriverById(dto.getDriverId());

        DriverLocation location = new DriverLocation();
        location.setDriver(driver);
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setTimestamp(java.time.LocalDateTime.now());

        return locationRepo.save(location);
    }

    @GetMapping("/location")
    public List<DriverLocation> getAllLocations() {
        return locationRepo.findAll();
    }

    @GetMapping("/{id}/rating")
    public double getDriverRating(@PathVariable Long id) {
        return driverService.getDriverRating(id);
    }

    @GetMapping("/{id}/location")
    public DriverLocation getDriverLocation(@PathVariable Long id) {
        return locationRepo.findAll()
                .stream()
                .filter(l -> l.getDriver().getId().equals(id))
                .reduce((first, second) -> second)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }
    
    @GetMapping("/{id}/bookings")
    public List<Booking> getDriverBookings(
            @PathVariable Long id) {

        return driverService.getDriverBookings(id);
    }
    
    @GetMapping("/{id}/completed-trips")
    public List<Booking> getCompletedTrips(
            @PathVariable Long id) {

        return driverService.getCompletedTrips(id);
    }
    
    @GetMapping("/{id}/earnings")
    public double getDriverEarnings(
            @PathVariable Long id) {

        return driverService.getDriverEarnings(id);
    } 
    
    @PutMapping("/{id}/online")
    public Driver goOnline(@PathVariable Long id) {
        return driverService.goOnline(id);
    }

    @PutMapping("/{id}/offline")
    public Driver goOffline(@PathVariable Long id) {
        return driverService.goOffline(id);
    }
}





