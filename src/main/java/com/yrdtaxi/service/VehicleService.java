package com.yrdtaxi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yrdtaxi.model.Vehicle;
import com.yrdtaxi.repository.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepo;

    public Vehicle save(Vehicle vehicle){
        return vehicleRepo.save(vehicle);
    }

    public List<Vehicle> getAll(){
        return vehicleRepo.findAll();
    }

    public Vehicle get(Long id){
        return vehicleRepo.findById(id)
                .orElseThrow();
    }

    public void delete(Long id){
        vehicleRepo.deleteById(id);
    }

}

