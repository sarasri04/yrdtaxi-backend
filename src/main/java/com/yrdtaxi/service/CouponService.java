package com.yrdtaxi.service;

import com.yrdtaxi.model.Coupon;
import com.yrdtaxi.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepo;

    public Coupon addCoupon(Coupon coupon) {
        return couponRepo.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    public double applyCoupon(String code, double amount) {

        Coupon coupon = couponRepo.findByCodeIgnoreCase(code);

        if (coupon == null || !coupon.isActive()) {
            throw new RuntimeException("Invalid coupon code");
        }

        double discount = 0;

        if ("FLAT".equalsIgnoreCase(coupon.getType())) {
            discount = coupon.getValue();
        } else if ("PERCENT".equalsIgnoreCase(coupon.getType())) {
            discount = amount * coupon.getValue() / 100;
        }

        double finalAmount = amount - discount;

        return Math.max(finalAmount, 0);
    }
    
    public void deleteCoupon(Long id) {
        couponRepo.deleteById(id);
    }
    
    public Coupon getCouponById(Long id) {
        return couponRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public Coupon updateCoupon(Long id, Coupon updatedCoupon) {
        Coupon coupon = getCouponById(id);

        coupon.setCode(updatedCoupon.getCode());
        coupon.setType(updatedCoupon.getType());
        coupon.setValue(updatedCoupon.getValue());
        coupon.setActive(updatedCoupon.isActive());

        return couponRepo.save(coupon);
    }
}



