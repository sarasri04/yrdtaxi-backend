package com.yrdtaxi.service;


import com.yrdtaxi.model.RoutePricing;
import com.yrdtaxi.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepo;

    // ➕ Add route pricing
    public RoutePricing saveRoute(RoutePricing route) {
        return routeRepo.save(route);
    }

    // 📄 Get all routes
    public List<RoutePricing> getAllRoutes() {
        return routeRepo.findAll();
    }

    // 🔍 Find route pricing
    public RoutePricing getRoute(String from, String to) {
        return routeRepo.findAll()
                .stream()
                .filter(r -> r.getFromLocation().equalsIgnoreCase(from)
                        && r.getToLocation().equalsIgnoreCase(to))
                .findFirst()
                .orElse(null);
    }
    
    public void deleteRoute(Long id) {
        routeRepo.deleteById(id);
    }
}


