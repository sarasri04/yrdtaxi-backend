package com.yrdtaxi.controller;


import com.yrdtaxi.model.Payment;
import com.yrdtaxi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public Payment pay(@RequestParam Long bookingId,
                       @RequestParam double amount) {

        return paymentService.processPayment(bookingId, amount);
    }
}





