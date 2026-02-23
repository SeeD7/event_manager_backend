package com.zeromus.eventmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventManagerApplication {

    private EventManagerApplication() {}

    static void main(String[] args) {
        SpringApplication.run(EventManagerApplication.class, args);
    }

}
