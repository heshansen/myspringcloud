package com.topbaby.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RegistryApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RegistryApplication.class, args);
    }
}
