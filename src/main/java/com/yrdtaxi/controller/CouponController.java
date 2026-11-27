package com.yrdtaxi.controller;


import com.yrdtaxi.model.Coupon;
import com.yrdtaxi.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    public Coupon addCoupon(@RequestBody Coupon coupon) {
        return couponService.addCoupon(coupon);
    }

    @GetMapping
    public List<Coupon> getCoupons() {
        return couponService.getAllCoupons();
    }

    @GetMapping("/apply")
    public double applyCoupon(
            @RequestParam String code,
            @RequestParam double amount
    ) {
        return couponService.applyCoupon(code, amount);
    }
    
    @DeleteMapping("/{id}")
    public String deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return "Coupon deleted successfully";
    }
    
    @PutMapping("/{id}")
    public Coupon updateCoupon(
            @PathVariable Long id,
            @RequestBody Coupon coupon
    ) {
        return couponService.updateCoupon(id, coupon);
    }
}


