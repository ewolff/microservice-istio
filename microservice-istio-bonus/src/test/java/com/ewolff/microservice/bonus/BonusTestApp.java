package com.ewolff.microservice.bonus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BonusTestApp {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BonusTestApp.class);
		app.setAdditionalProfiles("test");
		app.run(args);
	}

}
