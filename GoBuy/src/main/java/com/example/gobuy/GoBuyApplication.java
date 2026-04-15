package com.example.gobuy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GoBuyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoBuyApplication.class, args);
    }

}
