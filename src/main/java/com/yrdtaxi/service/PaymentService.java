package com.yrdtaxi.service;


import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Payment;
import com.yrdtaxi.model.BookingStatus;
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

    // 📄 GET PAYMENT
    public Payment getPaymentById(Long id) {
        return paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
    }

    // 💳 PROCESS PAYMENT
    public Payment processPayment(Long bookingId, double amount) {

        // 🔍 Step 1: Find Booking
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // ❌ Step 2: Prevent duplicate payment
        if (booking.getStatus() == BookingStatus.PAID) {
            throw new RuntimeException("Payment already done");
        }

        // ❌ Step 3: Ensure ride completed
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException("Ride not completed yet");
        }

        // 💳 Step 4: Create payment
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setStatus("SUCCESS");
        payment.setBooking(booking);
        payment.setPaymentTime(LocalDateTime.now());

        // 🔄 Step 5: Update booking status
        booking.setStatus(BookingStatus.PAID);
        bookingRepo.save(booking);

        // 💾 Step 6: Save payment
        return paymentRepo.save(payment);
    }
}






