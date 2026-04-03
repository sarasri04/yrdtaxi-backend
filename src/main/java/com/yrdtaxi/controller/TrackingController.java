package com.yrdtaxi.controller;


import com.yrdtaxi.dto.DriverLocationDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TrackingController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Driver sends location
    @MessageMapping("/driver-location")
    public void updateLocation(DriverLocationDTO location) {

        // Broadcast to all users
        messagingTemplate.convertAndSend(
                "/topic/driver-location",
                location
        );
    }
}


