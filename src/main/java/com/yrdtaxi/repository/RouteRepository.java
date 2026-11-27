package com.yrdtaxi.repository;


import com.yrdtaxi.model.RoutePricing;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<RoutePricing, Long> {
	
	Optional<RoutePricing> findByFromLocationIgnoreCaseAndToLocationIgnoreCase(
	        String fromLocation,
	        String toLocation
	);
}



