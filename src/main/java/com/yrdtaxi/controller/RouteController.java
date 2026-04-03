package com.yrdtaxi.controller;


import com.yrdtaxi.model.RoutePricing;
import com.yrdtaxi.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin("*")
public class RouteController {

    @Autowired
    private RouteService routeService;

    // ➕ Add route
    @PostMapping
    public RoutePricing addRoute(@RequestBody RoutePricing route) {
        return routeService.saveRoute(route);
    }

    // 📄 Get all routes
    @GetMapping
    public List<RoutePricing> getRoutes() {
        return routeService.getAllRoutes();
    }

    // 🔍 Get specific route
    @GetMapping("/search")
    public RoutePricing getRoute(@RequestParam String from,
                                @RequestParam String to) {
        return routeService.getRoute(from, to);
    }
}

