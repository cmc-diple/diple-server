package com.example.deple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DepleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepleApplication.class, args);
    }

}
