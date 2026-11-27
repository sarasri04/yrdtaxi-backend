package com.yrdtaxi.repository;


import java.util.Optional;
import com.yrdtaxi.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
	
	  Optional<Driver> findFirstByOnlineTrueAndAvailableTrue();
}

