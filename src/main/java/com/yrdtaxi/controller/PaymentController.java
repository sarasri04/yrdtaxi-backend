package com.yrdtaxi.controller;


import com.yrdtaxi.dto.PaymentRequest;
import com.yrdtaxi.model.Payment;
import com.yrdtaxi.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
//@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // 💳 MAKE PAYMENT (FIXED)
    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequest request) {

        try {
            System.out.println("Booking ID: " + request.getBookingId());
            System.out.println("Amount: " + request.getAmount());

            Payment payment = paymentService.processPayment(
                    request.getBookingId(),
                    request.getAmount()
            );

            return ResponseEntity.ok(payment);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 📄 GET PAYMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(@PathVariable Long id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}










