package com.yrdtaxi.repository;


import com.yrdtaxi.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}


