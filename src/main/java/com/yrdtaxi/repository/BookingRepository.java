package com.yrdtaxi.repository;


import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Driver;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	
	  List<Booking> findByDriver(Driver driver);

	    List<Booking> findByDriverAndStatus(
	            Driver driver,
	            String status
	    );
	    
	    List<Booking> findByCustomerPhone(String customerPhone);

	    List<Booking> findByCustomerEmail(String customerEmail);

	    List<Booking> findByStatus(String status);  
}


