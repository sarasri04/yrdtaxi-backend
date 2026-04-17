package com.yrdtaxi.service;


import com.yrdtaxi.model.BookingStatus;
import com.yrdtaxi.repository.BookingRepository;
import com.yrdtaxi.repository.DriverRepository;
import com.yrdtaxi.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private DriverRepository driverRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    // 📊 Total bookings
    public long totalBookings() {
        return bookingRepo.count();
    }

    // 💰 Total revenue
    public double totalRevenue() {
        return paymentRepo.findAll()
                .stream()
                .mapToDouble(p -> p.getAmount())
                .sum();
    }

    // 🚕 Total drivers
    public long totalDrivers() {
        return driverRepo.count();
    }

    // ✅ Completed rides
    public long completedRides() {
        return bookingRepo.findAll()
                .stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                .count();
    }

    // ❌ Cancelled rides
    public long cancelledRides() {
        return bookingRepo.findAll()
                .stream()
                .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
                .count();
    }
}



