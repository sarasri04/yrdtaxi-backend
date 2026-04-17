package com.yrdtaxi.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "rating")
public class Rating {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	       
	    private int rating;

	   
	    private String review;

	    @ManyToOne
	    private Booking booking;

	    @ManyToOne
	    private Driver driver;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
		}

		

		public String getReview() {
			return review;
		}

		public void setReview(String review) {
			this.review = review;
		}

		public Booking getBooking() {
			return booking;
		}

		public void setBooking(Booking booking) {
			this.booking = booking;
		}

		public Driver getDriver() {
			return driver;
		}

		public void setDriver(Driver driver) {
			this.driver = driver;
		}
	    
	}
  
    


