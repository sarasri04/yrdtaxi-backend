package com.yrdtaxi.controller;


import com.yrdtaxi.model.Rating;
import com.yrdtaxi.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin("*")
public class RatingController {

    @Autowired
    private RatingService service;

    @PostMapping
    public Rating save(@RequestBody Rating rating) {
        return service.save(rating);
    }
}


