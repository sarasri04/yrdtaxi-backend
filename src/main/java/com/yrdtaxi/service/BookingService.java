package com.yrdtaxi.service;


import com.yrdtaxi.model.*;
import com.yrdtaxi.repository.*;
import com.yrdtaxi.util.FareCalculator;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private DriverService driverService;

    @Autowired
    private SmsService smsService;  // ✅ SMS ADDED

    public Booking createBooking(Booking booking) {

        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("REQUESTED");

        // 🚕 Find nearest driver
        Driver driver = driverService.findNearestDriver(
                booking.getPickupLat(),
                booking.getPickupLng()
        );

        booking.setDriver(driver);

        // 💰 Fare calculation
        double baseFare = FareCalculator.calculate(booking.getDistance());

        booking.setBaseFare(baseFare);
        booking.setSurgeMultiplier(1.0);
        booking.setTotalFare(baseFare);

        // 💾 Save booking first
        Booking savedBooking = bookingRepo.save(booking);

        // 📱 SEND SMS TO CUSTOMER
        try {
            String message = "🚖 YRD Taxi Booking Confirmed!\n" +
                    "Pickup: " + booking.getPickupLocation() + "\n" +
                    "Drop: " + booking.getDropLocation() + "\n" +
                    "Fare: ₹" + booking.getTotalFare();

            if (driver != null) {
                message += "\nDriver: " + driver.getName() +
                           " (" + driver.getPhone() + ")";
            }

            smsService.sendSms(booking.getCustomerPhone(), message);

        } catch (Exception e) {
            System.out.println("SMS failed: " + e.getMessage());
        }

        return savedBooking;
    }
    
 // 🚕 Driver accepts ride
    public Booking acceptRide(Long bookingId, Long driverId) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Driver driver = driverService.getDriverById(driverId);

        booking.setDriver(driver);
        booking.setStatus("ACCEPTED");

        return bookingRepo.save(booking);
    }
    
 // 🚀 Start ride
    public Booking startRide(Long bookingId) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("STARTED");
        booking.setStartTime(LocalDateTime.now());

        return bookingRepo.save(booking);
    }
    
 // 🏁 Complete ride
    public Booking completeRide(Long bookingId) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("COMPLETED");
        booking.setBookingTime(LocalDateTime.now());

        return bookingRepo.save(booking);
    }
    
 // ❌ Cancel ride
    public Booking cancelRide(Long bookingId) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("CANCELLED");

        return bookingRepo.save(booking);
    }
}











