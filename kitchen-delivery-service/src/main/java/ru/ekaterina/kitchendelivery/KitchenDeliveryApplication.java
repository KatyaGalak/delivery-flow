package ru.ekaterina.kitchendelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;

@SpringBootApplication
@EnableProcessApplication
public class KitchenDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchenDeliveryApplication.class, args);
    }
}
