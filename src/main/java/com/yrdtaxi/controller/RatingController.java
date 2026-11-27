package com.yrdtaxi.controller;


import com.yrdtaxi.dto.RatingRequest;
import com.yrdtaxi.model.Rating;
import com.yrdtaxi.service.RatingService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService service;

    @PostMapping
    public Rating save(@RequestBody RatingRequest request) {
        return service.save(request);
    }

    @GetMapping("/driver/{driverId}")
    public List<Rating> getByDriver(@PathVariable Long driverId) {
        return service.getByDriver(driverId);
    }

    @GetMapping("/driver/{driverId}/average")
    public double getAverage(@PathVariable Long driverId) {
        return service.getAverageRating(driverId);
    }
}





