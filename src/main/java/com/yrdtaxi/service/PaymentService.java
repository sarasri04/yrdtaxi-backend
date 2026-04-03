package com.yrdtaxi.service;


import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Payment;
import com.yrdtaxi.repository.BookingRepository;
import com.yrdtaxi.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private BookingRepository bookingRepo;

    // 💳 Process Payment
    public Payment processPayment(Long bookingId, double amount) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setStatus("SUCCESS"); // later integrate Razorpay
        payment.setBooking(booking);

        // Optional: add timestamp if you have field
        // payment.setPaymentTime(LocalDateTime.now());

        // Update booking status after payment
        booking.setStatus("COMPLETED");

        bookingRepo.save(booking);

        return paymentRepo.save(payment);
    }
}


