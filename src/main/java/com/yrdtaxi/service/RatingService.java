package com.yrdtaxi.service;


import com.yrdtaxi.model.Rating;
import com.yrdtaxi.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private RatingRepository repo;

    public Rating save(Rating rating) {
        return repo.save(rating);
    }
}


