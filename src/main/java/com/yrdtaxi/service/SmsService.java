package com.yrdtaxi.service;


import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendSms(String phone, String message) {

        // 🔥 Simulated SMS
        System.out.println("Sending SMS to: " + phone);
        System.out.println("Message: " + message);

        // Later integrate:
        // Twilio / Fast2SMS / AWS SNS
    }
}


