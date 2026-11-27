package com.yrdtaxi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yrdtaxi.model.Vehicle;
import com.yrdtaxi.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @PostMapping
    public Vehicle save(@RequestBody Vehicle vehicle){
        return service.save(vehicle);
    }

    @GetMapping
    public List<Vehicle> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Vehicle get(@PathVariable Long id){
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

}

