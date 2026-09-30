package com.example.aladinservice;

import com.example.common.adapter.ApiControllerAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@Import(ApiControllerAdvice.class)
@SpringBootApplication
public class AladinServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AladinServiceApplication.class, args);
    }
}
