package com.yrdtaxi.repository;


import com.yrdtaxi.model.Rating;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
	List<Rating> findByDriverId(Long driverId);
	
}


