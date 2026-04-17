package com.yrdtaxi.service;


import com.yrdtaxi.model.*;
import com.yrdtaxi.repository.*;
import com.yrdtaxi.util.FareCalculator;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BookingService {

	    @Autowired
	    private BookingRepository bookingRepo;

	    @Autowired
	    private DriverService driverService;

	    @Autowired
	    private SmsService smsService;
	    
	    @Autowired
	    private RouteService routeService;

	    public Booking createBooking(Booking booking) {

	        booking.setBookingTime(LocalDateTime.now());
	        booking.setStatus(BookingStatus.REQUESTED); // ✅ FIXED

	        // 🚕 Find nearest driver
	        Driver driver = driverService.findNearestDriver(
	                booking.getPickupLat(),
	                booking.getPickupLng()
	        );

	        booking.setDriver(driver);
	        
	        // 💰 GET ROUTE PRICING
	        RoutePricing route = routeService.getRoute(
	                booking.getPickupLocation(),
	                booking.getDropLocation()
	        );


	        // 💰 Fare calculation
	        double baseFare = FareCalculator.calculate(booking.getDistance());

	        booking.setBaseFare(baseFare);
	        booking.setSurgeMultiplier(1.0);
	        booking.setTotalFare(baseFare);

	        Booking savedBooking = bookingRepo.save(booking);

	        // 📱 SEND SMS
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
	        booking.setStatus(BookingStatus.ACCEPTED); // ✅ FIXED

	        return bookingRepo.save(booking);
	    }

	    // 🚀 Start ride
	    public Booking startRide(Long bookingId) {

	        Booking booking = bookingRepo.findById(bookingId)
	                .orElseThrow(() -> new RuntimeException("Booking not found"));

	        booking.setStatus(BookingStatus.STARTED); // ✅ FIXED
	        booking.setStartTime(LocalDateTime.now());

	        return bookingRepo.save(booking);
	    }

	    // 🏁 Complete ride
	    public Booking completeRide(Long bookingId) {

	        Booking booking = bookingRepo.findById(bookingId)
	                .orElseThrow(() -> new RuntimeException("Booking not found"));

	        booking.setStatus(BookingStatus.COMPLETED); // ✅ FIXED
	        booking.setBookingTime(LocalDateTime.now());

	        return bookingRepo.save(booking);
	    }

	    // ❌ Cancel ride
	    public Booking cancelRide(Long bookingId) {

	        Booking booking = bookingRepo.findById(bookingId)
	                .orElseThrow(() -> new RuntimeException("Booking not found"));

	        booking.setStatus(BookingStatus.CANCELLED); // ✅ FIXED

	        return bookingRepo.save(booking);
	    }

	    public List<Booking> getAllBookings() {
	        return bookingRepo.findAll();
	    }

	    public Booking getBookingById(Long id) {
	        return bookingRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Booking not found"));
	    }
	    
	    
	}


        









