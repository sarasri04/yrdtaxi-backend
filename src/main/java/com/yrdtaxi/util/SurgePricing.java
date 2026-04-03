package com.yrdtaxi.util;


public class SurgePricing {

    public static double apply(double fare, boolean peak) {
        return peak ? fare * 1.5 : fare;
    }
}

