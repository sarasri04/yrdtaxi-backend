package com.yrdtaxi.dto;


import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private double amount;
	public Long getBookingId() {
		return bookingId;
	}
	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
    
}


