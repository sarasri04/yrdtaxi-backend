package com.yrdtaxi.controller;


import com.yrdtaxi.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin("*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService service;

    @GetMapping("/total-bookings")
    public long totalBookings() {
        return service.totalBookings();
    }
}


