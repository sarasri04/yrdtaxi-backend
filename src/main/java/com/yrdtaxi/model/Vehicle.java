package com.yrdtaxi.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleNumber;

    private String model;

    private String brand;

    private String color;

    private String fuelType;

    private Integer seatCapacity;

    private String insuranceNumber;

    private String permitNumber;

    private String rcNumber;

    private String fitnessCertificate;

    private String pollutionCertificate;

    private String vehicleStatus; // ACTIVE, SERVICE, INACTIVE

    @OneToOne
    @JoinColumn(name="driver_id")
    private Driver driver;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public Integer getSeatCapacity() {
		return seatCapacity;
	}

	public void setSeatCapacity(Integer seatCapacity) {
		this.seatCapacity = seatCapacity;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public String getPermitNumber() {
		return permitNumber;
	}

	public void setPermitNumber(String permitNumber) {
		this.permitNumber = permitNumber;
	}

	public String getRcNumber() {
		return rcNumber;
	}

	public void setRcNumber(String rcNumber) {
		this.rcNumber = rcNumber;
	}

	public String getFitnessCertificate() {
		return fitnessCertificate;
	}

	public void setFitnessCertificate(String fitnessCertificate) {
		this.fitnessCertificate = fitnessCertificate;
	}

	public String getPollutionCertificate() {
		return pollutionCertificate;
	}

	public void setPollutionCertificate(String pollutionCertificate) {
		this.pollutionCertificate = pollutionCertificate;
	}

	public String getVehicleStatus() {
		return vehicleStatus;
	}

	public void setVehicleStatus(String vehicleStatus) {
		this.vehicleStatus = vehicleStatus;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
    
    
}

