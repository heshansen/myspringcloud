package com.topbaby.cloud.demoresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoResourceServerApplication.class, args);
	}
}
