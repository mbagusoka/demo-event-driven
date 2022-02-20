package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RunnerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunnerAppApplication.class, args);
    }

}
