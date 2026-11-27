package com.yrdtaxi.model;


import jakarta.persistence.*;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String type; // FLAT / PERCENT
    private double value;
    private boolean active = true;

    public Long getId() { return id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}


