package com.yrdtaxi.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import com.yrdtaxi.dto.PaymentRequest;
import com.yrdtaxi.dto.PaymentResponse;
import com.yrdtaxi.dto.PaymentVerifyRequest;

import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Payment;
import com.yrdtaxi.model.PaymentStatus;

import com.yrdtaxi.repository.BookingRepository;
import com.yrdtaxi.repository.PaymentRepository;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;


    // =========================================================
    // 1. GET PAYMENT BY ID
    // =========================================================
    public Payment getPaymentById(Long id) {

        return paymentRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found with ID: " + id
                        )
                );
    }


    // =========================================================
    // 2. NORMAL PAYMENT
    // POST /api/payments
    // =========================================================
    public Payment processPayment(
            Long bookingId,
            double amount,
            String paymentMethod) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with ID: " + bookingId
                        )
                );


        // Prevent duplicate payment
        if (Boolean.TRUE.equals(booking.getPaid())) {

            throw new RuntimeException(
                    "Payment already completed"
            );
        }


        // Ride must be completed
        if (!"COMPLETED".equalsIgnoreCase(
                booking.getStatus())) {

            throw new RuntimeException(
                    "Ride not completed yet"
            );
        }


        // Create payment
        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentTime(LocalDateTime.now());

        // Existing String status
        payment.setStatus("SUCCESS");

        // Payment enum status
        payment.setPaymentStatus(
                PaymentStatus.SUCCESS
        );


        // Update booking
        booking.setPaid(true);
        booking.setStatus("PAID");

        bookingRepo.save(booking);

        return paymentRepo.save(payment);
    }


    // =========================================================
    // 3. CREATE RAZORPAY ORDER
    // POST /api/payments/create-order
    // =========================================================
    public PaymentResponse createOrder(
            PaymentRequest request) throws Exception {

        if (request.getBookingId() == null) {

            throw new RuntimeException(
                    "Booking ID is required"
            );
        }


        if (request.getAmount() <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than zero"
            );
        }


        // Check booking
        Booking booking = bookingRepo.findById(
                request.getBookingId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Booking not found with ID: "
                                + request.getBookingId()
                )
        );


        // Prevent duplicate payment
        if (Boolean.TRUE.equals(booking.getPaid())) {

            throw new RuntimeException(
                    "Payment already completed"
            );
        }


        // Create Razorpay order
        JSONObject orderRequest = new JSONObject();

        // Razorpay uses paise
        int amountInPaise =
                (int) Math.round(
                        request.getAmount() * 100
                );

        orderRequest.put(
                "amount",
                amountInPaise
        );

        orderRequest.put(
                "currency",
                "INR"
        );

        orderRequest.put(
                "receipt",
                "BOOKING_" + request.getBookingId()
        );


        Order order =
                razorpayClient.orders.create(
                        orderRequest
                );


        // Response
        PaymentResponse response =
                new PaymentResponse();

        response.setOrderId(
                order.get("id")
        );

        response.setAmount(
                request.getAmount()
        );

        response.setCurrency(
                "INR"
        );

        response.setKey(
                razorpayKey
        );

        return response;
    }


    // =========================================================
    // 4. VERIFY RAZORPAY PAYMENT
    // POST /api/payments/verify
    // =========================================================
    public void verifyPayment(
            PaymentVerifyRequest request)
            throws Exception {

        Booking booking = bookingRepo.findById(
                request.getBookingId()
        ).orElseThrow(() ->
                new RuntimeException(
                        "Booking not found with ID: "
                                + request.getBookingId()
                )
        );


        // Prevent duplicate payment
        if (Boolean.TRUE.equals(booking.getPaid())) {

            throw new RuntimeException(
                    "Payment already completed"
            );
        }


        // Validate Razorpay order ID
        if (request.getRazorpayOrderId() == null
                || request.getRazorpayOrderId()
                        .trim()
                        .isEmpty()) {

            throw new RuntimeException(
                    "Razorpay Order ID is required"
            );
        }


        // Validate Razorpay payment ID
        if (request.getRazorpayPaymentId() == null
                || request.getRazorpayPaymentId()
                        .trim()
                        .isEmpty()) {

            throw new RuntimeException(
                    "Razorpay Payment ID is required"
            );
        }


        // Validate Razorpay signature
        if (request.getRazorpaySignature() == null
                || request.getRazorpaySignature()
                        .trim()
                        .isEmpty()) {

            throw new RuntimeException(
                    "Razorpay Signature is required"
            );
        }


        // Razorpay signature data
        JSONObject options =
                new JSONObject();

        options.put(
                "razorpay_order_id",
                request.getRazorpayOrderId()
        );

        options.put(
                "razorpay_payment_id",
                request.getRazorpayPaymentId()
        );

        options.put(
                "razorpay_signature",
                request.getRazorpaySignature()
        );


        // Verify signature
        boolean valid =
                Utils.verifyPaymentSignature(
                        options,
                        razorpayKeySecret
                );


        if (!valid) {

            throw new RuntimeException(
                    "Invalid Razorpay Signature"
            );
        }


        // Create payment
        Payment payment = new Payment();

        payment.setBooking(booking);

        payment.setAmount(
                booking.getTotalFare()
        );

        payment.setPaymentMethod(
                "RAZORPAY"
        );

        payment.setPaymentTime(
                LocalDateTime.now()
        );

        payment.setStatus(
                "SUCCESS"
        );

        payment.setPaymentStatus(
                PaymentStatus.SUCCESS
        );


        // Save Razorpay details
        payment.setRazorpayOrderId(
                request.getRazorpayOrderId()
        );

        payment.setRazorpayPaymentId(
                request.getRazorpayPaymentId()
        );

        payment.setRazorpaySignature(
                request.getRazorpaySignature()
        );


        // Save payment
        paymentRepo.save(payment);


        // Update booking
        booking.setPaid(true);
        booking.setStatus("PAID");

        bookingRepo.save(booking);
    }


    // =========================================================
    // 5. SUBMIT BANK TRANSFER
    // POST /api/payments/bank-transfer
    // =========================================================
    public Payment submitBankTransfer(
            Long bookingId,
            String transactionId,
            String paymentProof) {

        Booking booking = bookingRepo.findById(
                bookingId
        ).orElseThrow(() ->
                new RuntimeException(
                        "Booking not found with ID: "
                                + bookingId
                )
        );


        // Transaction ID required
        if (transactionId == null
                || transactionId.trim().isEmpty()) {

            throw new RuntimeException(
                    "Transaction ID is required"
            );
        }


        // Prevent duplicate payment
        if (Boolean.TRUE.equals(
                booking.getPaid())) {

            throw new RuntimeException(
                    "Payment already completed"
            );
        }


        Payment payment = new Payment();

        payment.setBooking(booking);

        payment.setAmount(
                booking.getTotalFare()
        );

        payment.setPaymentMethod(
                "BANK_TRANSFER"
        );

        payment.setTransactionId(
                transactionId.trim()
        );

        payment.setPaymentProof(
                paymentProof
        );

        payment.setPaymentStatus(
                PaymentStatus.PENDING
        );

        payment.setStatus(
                "PENDING"
        );

        payment.setPaymentTime(
                LocalDateTime.now()
        );


        // Booking remains unpaid
        // until admin approves payment
        return paymentRepo.save(payment);
    }


    // =========================================================
    // 6. GET PENDING PAYMENTS
    // GET /api/payments/pending
    // =========================================================
    public List<Payment> getPendingPayments() {

        return paymentRepo.findByPaymentStatus(
                PaymentStatus.PENDING
        );
    }


    // =========================================================
    // 7. APPROVE BANK PAYMENT
    // PUT /api/payments/{id}/approve
    // =========================================================
    public Payment approveBankPayment(
            Long paymentId) {

        Payment payment =
                paymentRepo.findById(paymentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with ID: "
                                                + paymentId
                                )
                        );


        // Must be bank transfer
        if (!"BANK_TRANSFER".equals(
                payment.getPaymentMethod())) {

            throw new RuntimeException(
                    "This payment is not a bank transfer"
            );
        }


        // Must be pending
        if (payment.getPaymentStatus()
                != PaymentStatus.PENDING) {

            throw new RuntimeException(
                    "This payment has already been processed"
            );
        }


        // Mark payment successful
        payment.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        payment.setStatus(
                "SUCCESS"
        );

        payment.setPaymentTime(
                LocalDateTime.now()
        );


        // Update booking
        Booking booking =
                payment.getBooking();

        booking.setPaid(true);
        booking.setStatus("PAID");

        bookingRepo.save(booking);


        return paymentRepo.save(payment);
    }


    // =========================================================
    // 8. REJECT BANK PAYMENT
    // PUT /api/payments/{id}/reject
    // =========================================================
    public Payment rejectBankPayment(
            Long paymentId) {

        Payment payment =
                paymentRepo.findById(paymentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found with ID: "
                                                + paymentId
                                )
                        );


        // Must be bank transfer
        if (!"BANK_TRANSFER".equals(
                payment.getPaymentMethod())) {

            throw new RuntimeException(
                    "This payment is not a bank transfer"
            );
        }


        // Must be pending
        if (payment.getPaymentStatus()
                != PaymentStatus.PENDING) {

            throw new RuntimeException(
                    "This payment has already been processed"
            );
        }


        // Mark payment failed
        payment.setPaymentStatus(
                PaymentStatus.FAILED
        );

        payment.setStatus(
                "FAILED"
        );

        payment.setPaymentTime(
                LocalDateTime.now()
        );


        return paymentRepo.save(payment);
    }
}




