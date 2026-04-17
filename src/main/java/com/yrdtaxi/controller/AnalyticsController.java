package com.yrdtaxi.controller;


import com.yrdtaxi.service.AnalyticsService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
//@CrossOrigin("*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService service;

    @GetMapping("/total-bookings")
    public long totalBookings() {
        return service.totalBookings();
    }

    @GetMapping("/total-revenue")
    public double totalRevenue() {
        return service.totalRevenue();
    }

    @GetMapping("/total-drivers")
    public long totalDrivers() {
        return service.totalDrivers();
    }

    @GetMapping("/completed-rides")
    public long completedRides() {
        return service.completedRides();
    }

    @GetMapping("/cancelled-rides")
    public long cancelledRides() {
        return service.cancelledRides();
    }
    
    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        Map<String, Object> data = new HashMap<>();

        data.put("totalBookings", service.totalBookings());
        data.put("totalRevenue", service.totalRevenue());
        data.put("totalDrivers", service.totalDrivers());
        data.put("completedRides", service.completedRides());
        data.put("cancelledRides", service.cancelledRides());

        return data;
    }
}





