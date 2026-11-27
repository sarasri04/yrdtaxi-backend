package com.yrdtaxi.repository;


import com.yrdtaxi.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCodeIgnoreCase(String code);
}

