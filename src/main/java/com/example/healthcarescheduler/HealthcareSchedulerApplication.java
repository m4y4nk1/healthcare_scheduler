package com.example.healthcarescheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthcareSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareSchedulerApplication.class, args);
    }
}