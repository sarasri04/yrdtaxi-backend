package com.yrdtaxi.controller;


import com.yrdtaxi.model.Booking;
import com.yrdtaxi.service.BookingService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bookings")
//@CrossOrigin("*")
public class BookingController {

	    @Autowired
	    private BookingService bookingService;

	    // ✅ CREATE BOOKING
	    @PostMapping
	    public Booking createBooking(@RequestBody Booking booking) {
	        return bookingService.createBooking(booking);
	    }

	    // 🚕 Accept ride
	    @PutMapping("/{id}/accept")
	    public Booking accept(@PathVariable Long id,
	                          @RequestParam Long driverId) {
	        return bookingService.acceptRide(id, driverId);
	    }

	    // 🚀 Start ride
	    @PutMapping("/{id}/start")
	    public Booking start(@PathVariable Long id) {
	        return bookingService.startRide(id);
	    }

	    // 🏁 Complete ride
	    @PutMapping("/{id}/complete")
	    public Booking complete(@PathVariable Long id) {
	        return bookingService.completeRide(id);
	    }

	    // ❌ Cancel ride
	    @PutMapping("/{id}/cancel")
	    public Booking cancel(@PathVariable Long id) {
	        return bookingService.cancelRide(id);
	    }

	    // ✅ GET ALL BOOKINGS
	    @GetMapping
	    public List<Booking> getAllBookings() {
	        return bookingService.getAllBookings();
	    }

	    // ✅ GET BOOKING BY ID
	    @GetMapping("/{id}")
	    public Booking getBooking(@PathVariable Long id) {
	        return bookingService.getBookingById(id);
	    }

	    // 🧪 TEST
	    @GetMapping("/test")
	    public String testMethod() {
	        return "Test successful";
	    }
	}
 




