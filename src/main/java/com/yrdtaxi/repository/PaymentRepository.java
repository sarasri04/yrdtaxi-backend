package com.yrdtaxi.repository;


import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import com.yrdtaxi.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
}


