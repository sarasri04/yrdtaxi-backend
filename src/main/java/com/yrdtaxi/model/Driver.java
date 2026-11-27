package com.yrdtaxi.model;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private boolean available;
    private Boolean online = false;

    private Double currentLat = 0.0;

    private Double currentLng = 0.0;

    private LocalDateTime lastSeen;
    
    @OneToOne(mappedBy = "driver")
    private Vehicle vehicle;
    
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public Boolean getOnline() {
		return online;
	}
	public void setOnline(Boolean online) {
		this.online = online;
	}
	public Double getCurrentLat() {
		return currentLat;
	}
	public void setCurrentLat(Double currentLat) {
		this.currentLat = currentLat;
	}
	public Double getCurrentLng() {
		return currentLng;
	}
	public void setCurrentLng(Double currentLng) {
		this.currentLng = currentLng;
	}
	public LocalDateTime getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(LocalDateTime lastSeen) {
		this.lastSeen = lastSeen;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
    
}

