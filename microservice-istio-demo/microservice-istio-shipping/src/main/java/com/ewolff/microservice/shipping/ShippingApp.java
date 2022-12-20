package com.ewolff.microservice.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ImportRuntimeHints(NativeRuntimeHints.class)
public class ShippingApp {

	public static void main(String[] args) {
		SpringApplication.run(ShippingApp.class, args);
	}

}
