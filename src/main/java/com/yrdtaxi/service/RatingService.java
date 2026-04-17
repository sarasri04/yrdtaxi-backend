package com.yrdtaxi.service;


import com.yrdtaxi.model.Rating;
import com.yrdtaxi.repository.RatingRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

	    @Autowired
	    private RatingRepository ratingRepo;

	    public double getAverageRating(Long driverId) {

	        List<Rating> ratings = ratingRepo.findByDriverId(driverId);

	        if (ratings.isEmpty()) return 0;

	        return ratings.stream()
	                .mapToInt(Rating::getRating)
	                .average()
	                .orElse(0);
	    }
	    
	    public List<Rating> getByDriver(Long driverId) {
	        return ratingRepo.findByDriverId(driverId);
	    }
	    
	    public Rating save(Rating rating) {

	        if (rating.getRating() < 1 || rating.getRating() > 5) {
	            throw new RuntimeException("Rating must be between 1 and 5");
	        }

	        return ratingRepo.save(rating);
	    }
	}
	



