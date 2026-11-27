
package com.yrdtaxi.controller;

import com.yrdtaxi.model.Booking;
import com.yrdtaxi.service.BookingService;
import com.yrdtaxi.service.InvoiceService;
import com.yrdtaxi.service.WhatsAppService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private InvoiceService invoiceService;

    // CREATE BOOKING + AUTOMATIC ADMIN WHATSAPP MESSAGE
    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {

        // Save booking first
        Booking savedBooking = bookingService.createBooking(booking);

        // Notify admin after successful save
        try {
            whatsAppService.sendAdminBookingMessage(
                    savedBooking.getId(),
                    savedBooking.getCustomerName(),
                    savedBooking.getCustomerPhone(),
                    savedBooking.getPickupLocation(),
                    savedBooking.getDropLocation(),
                    savedBooking.getTripType(),
                    savedBooking.getTotalFare(),
                    Boolean.TRUE.equals(savedBooking.getPaid())
                            ? "PAID"
                            : "PAYMENT PENDING"
            );
        } catch (Exception e) {
            // WhatsApp failure must not undo the saved booking
            System.err.println(
                    "WhatsApp notification failed for booking ID: "
                            + savedBooking.getId()
                            + " - " + e.getMessage()
            );
        }

        return savedBooking;
    }

    // GET ALL BOOKINGS
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // GET BOOKING BY ID
    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    // ACCEPT RIDE
    @PutMapping("/{id}/accept")
    public Booking acceptRide(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        return bookingService.acceptRide(id, driverId);
    }

    // START RIDE
    @PutMapping("/{id}/start")
    public Booking startRide(@PathVariable Long id) {
        return bookingService.startRide(id);
    }

    // COMPLETE RIDE
    @PutMapping("/{id}/complete")
    public Booking completeRide(@PathVariable Long id) {
        return bookingService.completeRide(id);
    }

    // CANCEL RIDE
    @PutMapping("/{id}/cancel")
    public Booking cancelRide(@PathVariable Long id) {
        return bookingService.cancelRide(id);
    }

    // TEST API
    @GetMapping("/test")
    public String testMethod() {
        return "Booking API working";
    }

    // BOOKINGS BY CUSTOMER PHONE
    @GetMapping("/customer/phone/{phone}")
    public List<Booking> getBookingsByPhone(
            @PathVariable String phone) {
        return bookingService.getBookingsByPhone(phone);
    }

    // BOOKINGS BY CUSTOMER EMAIL
    @GetMapping("/customer/email/{email}")
    public List<Booking> getBookingsByEmail(
            @PathVariable String email) {
        return bookingService.getBookingsByEmail(email);
    }

    // BOOKINGS BY STATUS
    @GetMapping("/status/{status}")
    public List<Booking> getBookingsByStatus(
            @PathVariable String status) {
        return bookingService.getBookingsByStatus(status);
    }

    // TOTAL BOOKINGS
    @GetMapping("/analytics/total")
    public long getTotalBookings() {
        return bookingService.getTotalBookings();
    }

    // BOOKING COUNT BY STATUS
    @GetMapping("/analytics/status/{status}")
    public long getBookingCountByStatus(
            @PathVariable String status) {
        return bookingService.getBookingCountByStatus(status);
    }

    // TOTAL REVENUE
    @GetMapping("/analytics/revenue")
    public double getTotalRevenue() {
        return bookingService.getTotalRevenue();
    }

    // INVOICE
    @GetMapping("/{id}/invoice")
    public String getInvoice(@PathVariable Long id) {
        return bookingService.generateInvoice(id);
    }

    // DOWNLOAD INVOICE PDF
    @GetMapping("/{id}/invoice/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(
            @PathVariable Long id) {

        Booking booking = bookingService.getBookingById(id);
        byte[] pdf = invoiceService.generateInvoicePdf(booking);

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=invoice-" + id + ".pdf"
                )
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }

    // DRIVER ACCEPT
    @PutMapping("/{id}/driver-accept")
    public Booking driverAccept(@PathVariable Long id) {
        return bookingService.driverAcceptBooking(id);
    }

    // DRIVER REJECT
    @PutMapping("/{id}/driver-reject")
    public Booking driverReject(@PathVariable Long id) {
        return bookingService.driverRejectBooking(id);
    }

    // VERIFY RIDE OTP
    @PutMapping("/{id}/verify-otp")
    public Booking verifyRideOtp(
            @PathVariable Long id,
            @RequestParam String otp) {
        return bookingService.verifyRideOtp(id, otp);
    }

    // DRIVER REACHED
    @PutMapping("/{id}/driver-reached")
    public Booking driverReached(@PathVariable Long id) {
        return bookingService.driverReached(id);
    }
}







