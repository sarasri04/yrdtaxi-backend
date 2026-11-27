package com.yrdtaxi.service;


import com.yrdtaxi.dto.RatingRequest;
import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Rating;
import com.yrdtaxi.repository.BookingRepository;
import com.yrdtaxi.repository.RatingRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepo;

    @Autowired
    private BookingRepository bookingRepo;

    public Rating save(RatingRequest request) {

        if (request.getStars() < 1 || request.getStars() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Booking booking = bookingRepo.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!Boolean.TRUE.equals(booking.getPaid())) {
            throw new RuntimeException("Please complete payment before rating");
        }

        if (ratingRepo.findByBookingId(request.getBookingId()).isPresent()) {
            throw new RuntimeException("Rating already submitted for this booking");
        }

        Rating rating = new Rating();
        rating.setRating(request.getStars());
        rating.setReview(request.getReview());
        rating.setBooking(booking);
        rating.setDriver(booking.getDriver());
        booking.setRated(true);
        bookingRepo.save(booking);
        

        return ratingRepo.save(rating);
    }
    public double getAverageRating(Long driverId) {
        List<Rating> ratings = ratingRepo.findByDriverId(driverId);

        if (ratings.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;

        for (Rating r : ratings) {
            total += r.getRating();
        }

        return total / ratings.size();
    }

    public List<Rating> getByDriver(Long driverId) {
        return ratingRepo.findByDriverId(driverId);
    }
}






	



