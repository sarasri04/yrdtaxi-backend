package com.yrdtaxi.repository;


import com.yrdtaxi.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    // ⭐ Get all ratings of a driver
    List<Rating> findByDriverId(Long driverId);

    // 🚖 Get rating by booking
    Optional<Rating> findByBookingId(Long bookingId);
}




