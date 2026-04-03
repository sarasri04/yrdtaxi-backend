package com.yrdtaxi.service;


import com.yrdtaxi.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    @Autowired
    private BookingRepository repo;

    public long totalBookings() {
        return repo.count();
    }
}

