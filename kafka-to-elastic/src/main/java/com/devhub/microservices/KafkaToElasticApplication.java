package com.devhub.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaToElasticApplication {
    public static void main (String[] args)  throws IllegalArgumentException {
        SpringApplication.run(KafkaToElasticApplication.class, args);
    }
}