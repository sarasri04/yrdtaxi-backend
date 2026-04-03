package com.yrdtaxi.controller;


import com.yrdtaxi.model.DriverLocation;
import com.yrdtaxi.repository.DriverLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver-location")
@CrossOrigin("*")
public class DriverController {

    @Autowired
    private DriverLocationRepository repo;

    // 📍 Update driver location
    @PostMapping
    public DriverLocation updateLocation(@RequestBody DriverLocation location) {
        return repo.save(location);
    }

    // 📍 Get all drivers
    @GetMapping
    public Object getAll() {
        return repo.findAll();
    }
}


