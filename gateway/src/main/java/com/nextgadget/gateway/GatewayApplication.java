package com.nextgadget.gateway;

import com.nextgadget.gateway.security.JwtAuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
		System.out.println("API Gateway started");
	}

	@Bean
	public GlobalFilter customFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
		return jwtAuthenticationFilter;
	}
}
