package com.yrdtaxi.repository;


import com.yrdtaxi.model.DriverLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverLocationRepository extends JpaRepository<DriverLocation, Long> {
}

