package com.yrdtaxi.repository;


import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findByBooking(Booking booking);
}


