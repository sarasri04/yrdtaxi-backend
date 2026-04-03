package com.yrdtaxi.controller;


import com.yrdtaxi.model.Booking;
import com.yrdtaxi.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // 🚕 Accept ride
    @PostMapping("/{id}/accept")
    public Booking accept(@PathVariable Long id,
                          @RequestParam Long driverId) {
        return bookingService.acceptRide(id, driverId);
    }

    // 🚀 Start ride
    @PostMapping("/{id}/start")
    public Booking start(@PathVariable Long id) {
        return bookingService.startRide(id);
    }

    // 🏁 Complete ride
    @PostMapping("/{id}/complete")
    public Booking complete(@PathVariable Long id) {
        return bookingService.completeRide(id);
    }

    // ❌ Cancel ride
    @PostMapping("/{id}/cancel")
    public Booking cancel(@PathVariable Long id) {
        return bookingService.cancelRide(id);
    }
    
    @GetMapping("/test")
	public String testMethod() {
		return "Test successful";
	}
}



