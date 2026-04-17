package com.yrdtaxi.controller;


import com.yrdtaxi.dto.DriverLocationDTO;
import com.yrdtaxi.model.Driver;
import com.yrdtaxi.model.DriverLocation;
import com.yrdtaxi.repository.DriverLocationRepository;
import com.yrdtaxi.repository.DriverRepository;
import com.yrdtaxi.service.DriverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
//@CrossOrigin("*")
public class DriverController {

    @Autowired
    private DriverRepository driverRepo;
    
    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverLocationRepository locationRepo;

    // ✅ 1. ADD DRIVER
    @PostMapping
    public Driver addDriver(@RequestBody Driver driver) {
        return driverRepo.save(driver);
    }

    // ✅ 2. GET ALL DRIVERS
    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverRepo.findAll();
    }

    // ✅ 3. GET DRIVER BY ID
    @GetMapping("/{id}")
    public Driver getDriver(@PathVariable Long id) {
        return driverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

    // ✅ 4. UPDATE DRIVER AVAILABILITY
    @PutMapping("/{id}/availability")
    public Driver updateAvailability(@PathVariable Long id,
                                     @RequestParam boolean available) {

        Driver driver = driverRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setAvailable(available);
        return driverRepo.save(driver);
    }

    // 📍 5. UPDATE DRIVER LOCATION
    @PostMapping("/location")
    public DriverLocation updateLocation(@RequestBody DriverLocationDTO dto) {

        Driver driver = driverRepo.findById(dto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        DriverLocation location = new DriverLocation();
        location.setDriver(driver);
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setTimestamp(java.time.LocalDateTime.now());

        return locationRepo.save(location);
    }

    // 📍 6. GET ALL DRIVER LOCATIONS
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
                .reduce((first, second) -> second) // latest record
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }
}




