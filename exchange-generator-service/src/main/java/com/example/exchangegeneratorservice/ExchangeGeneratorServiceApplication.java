package com.example.exchangegeneratorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeGeneratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeGeneratorServiceApplication.class, args);
    }
}
