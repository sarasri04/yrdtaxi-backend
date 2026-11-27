package com.yrdtaxi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📍 Location
    private String pickupLocation;
    private String dropLocation;

    private double pickupLat;
    private double pickupLng;

    private double dropLat;
    private double dropLng;

    // 👤 Customer
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // 🚗 Trip
    private String tripType; // ONEWAY / ROUND
    private String cabType;  // MINI / SEDAN / SUV

    private double distance;
    private double duration;
    
    @Column(nullable = true)
    private Double driverBata = 0.0;

    @Column(nullable = true)
    private Double tollFees = 0.0;
    
    @Column(nullable = true)
    private Boolean paid = false;

    // 💰 Fare
    private double baseFare;
    private double surgeMultiplier;
    private double totalFare;

    // ✅ Payment flag (correct)
    
   

    // 📊 Status (BETTER → ENUM)
   // @Enumerated(EnumType.STRING)
    //private BookingStatus status;
    private String status;

    public Double getDriverBata() {
        return driverBata;
    }

    public void setDriverBata(Double driverBata) {
        this.driverBata = driverBata;
    }

    public Double getTollFees() {
        return tollFees;
    }

    public void setTollFees(Double tollFees) {
        this.tollFees = tollFees;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
    
    private Integer etaMinutes = 15;

	public Integer getEtaMinutes() {
		return etaMinutes;
	}

	public void setEtaMinutes(Integer etaMinutes) {
		this.etaMinutes = etaMinutes;
	}

	// ⏰ Time
    private LocalDateTime bookingTime;
    private LocalDateTime pickupTime;
    private LocalDateTime dropTime;

    private LocalDateTime startTime;   // ✅ ADD THIS
    private LocalDateTime endTime;     // ✅ (Recommended)
    
    private LocalDate pickupDate;
    public LocalDate getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(LocalDate pickupDate) {
		this.pickupDate = pickupDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	private LocalDate returnDate;
    
    
    public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	@Column(nullable = true)
	private Boolean rated = false;

	public Boolean getRated() {
	    return rated;
	}

	public void setRated(Boolean rated) {
	    this.rated = rated;
	}
	
	
	private String rideOtp;
	private Boolean otpVerified = false;

	public String getRideOtp() {
		return rideOtp;
	}

	public void setRideOtp(String rideOtp) {
		this.rideOtp = rideOtp;
	}

	public Boolean getOtpVerified() {
		return otpVerified;
	}

	public void setOtpVerified(Boolean otpVerified) {
		this.otpVerified = otpVerified;
	}

	// 🚕 Driver
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    // 👤 User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getDropLocation() {
		return dropLocation;
	}

	public void setDropLocation(String dropLocation) {
		this.dropLocation = dropLocation;
	}

	public double getPickupLat() {
		return pickupLat;
	}

	public void setPickupLat(double pickupLat) {
		this.pickupLat = pickupLat;
	}

	public double getPickupLng() {
		return pickupLng;
	}

	public void setPickupLng(double pickupLng) {
		this.pickupLng = pickupLng;
	}

	public double getDropLat() {
		return dropLat;
	}

	public void setDropLat(double dropLat) {
		this.dropLat = dropLat;
	}

	public double getDropLng() {
		return dropLng;
	}

	public void setDropLng(double dropLng) {
		this.dropLng = dropLng;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getCabType() {
		return cabType;
	}

	public void setCabType(String cabType) {
		this.cabType = cabType;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(double baseFare) {
		this.baseFare = baseFare;
	}

	public double getSurgeMultiplier() {
		return surgeMultiplier;
	}

	public void setSurgeMultiplier(double surgeMultiplier) {
		this.surgeMultiplier = surgeMultiplier;
	}

	public double getTotalFare() {
		return totalFare;
	}

	public void setTotalFare(double totalFare) {
		this.totalFare = totalFare;
	}

	public String getStatus() {
	    return status;
	}

	public void setStatus(String status) {
	    this.status = status;
	}


	public LocalDateTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(LocalDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}

	public LocalDateTime getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(LocalDateTime pickupTime) {
		this.pickupTime = pickupTime;
	}

	public LocalDateTime getDropTime() {
		return dropTime;
	}

	public void setDropTime(LocalDateTime dropTime) {
		this.dropTime = dropTime;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	


    
}







