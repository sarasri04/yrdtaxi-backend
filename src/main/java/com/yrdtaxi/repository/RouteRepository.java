package com.yrdtaxi.repository;


import com.yrdtaxi.model.RoutePricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<RoutePricing, Long> {
}


