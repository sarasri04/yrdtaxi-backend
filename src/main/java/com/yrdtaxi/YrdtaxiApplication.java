package com.yrdtaxi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@ConfigurationPropertiesScan
public class YrdtaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YrdtaxiApplication.class, args);
        System.out.println("🚀 YRD Taxi Backend Running...");
    }
}


