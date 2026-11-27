package com.yrdtaxi.controller;

import com.yrdtaxi.dto.PaymentRequest;
import com.yrdtaxi.dto.PaymentVerifyRequest;
import com.yrdtaxi.model.Payment;
import com.yrdtaxi.service.PaymentService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    // =========================================================
    // 1. NORMAL PAYMENT
    // POST /api/payments
    // =========================================================
    @PostMapping
    public ResponseEntity<?> makePayment(
            @RequestBody PaymentRequest request) {

        try {

            System.out.println("Booking ID: " + request.getBookingId());
            System.out.println("Amount: " + request.getAmount());
            System.out.println("Payment Method: "
                    + request.getPaymentMethod());

            Payment payment = paymentService.processPayment(
                    request.getBookingId(),
                    request.getAmount(),
                    request.getPaymentMethod()
            );

            return ResponseEntity.ok(payment);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // 2. GET PAYMENT BY ID
    // GET /api/payments/{id}
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(
            @PathVariable Long id) {

        try {

            Payment payment =
                    paymentService.getPaymentById(id);

            return ResponseEntity.ok(payment);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // 3. RAZORPAY CREATE ORDER
    // POST /api/payments/create-order
    // =========================================================
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestBody PaymentRequest request) {

        try {

            return ResponseEntity.ok(
                    paymentService.createOrder(request)
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // 4. RAZORPAY VERIFY PAYMENT
    // POST /api/payments/verify
    // =========================================================
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentVerifyRequest request) {

        try {

            paymentService.verifyPayment(request);

            return ResponseEntity.ok(
                    "Payment Verified Successfully"
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // 5. BANK TRANSFER SUBMISSION
    // POST /api/payments/bank-transfer
    // =========================================================
    @PostMapping("/bank-transfer")
    public ResponseEntity<?> submitBankTransfer(
            @RequestBody Map<String, Object> request) {

        try {

            if (request.get("bookingId") == null) {
                throw new RuntimeException(
                        "Booking ID is required"
                );
            }

            if (request.get("transactionId") == null) {
                throw new RuntimeException(
                        "Transaction ID is required"
                );
            }

            Long bookingId = Long.valueOf(
                    request.get("bookingId").toString()
            );

            String transactionId =
                    request.get("transactionId").toString();

            String paymentProof = null;

            if (request.get("paymentProof") != null) {

                paymentProof =
                        request.get("paymentProof").toString();
            }

            Payment payment =
                    paymentService.submitBankTransfer(
                            bookingId,
                            transactionId,
                            paymentProof
                    );

            return ResponseEntity.ok(payment);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // 6. GET PENDING BANK PAYMENTS
    // GET /api/payments/pending
    // =========================================================
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingPayments() {

        try {

            return ResponseEntity.ok(
                    paymentService.getPendingPayments()
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // 7. APPROVE BANK PAYMENT
    // PUT /api/payments/{id}/approve
    // =========================================================
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approvePayment(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    paymentService.approveBankPayment(id)
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // 8. REJECT BANK PAYMENT
    // PUT /api/payments/{id}/reject
    // =========================================================
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectPayment(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    paymentService.rejectBankPayment(id)
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}










