package com.ewolff.microservice.bonus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BonusApp {

	public static void main(String[] args) {
		SpringApplication.run(BonusApp.class, args);
	}

}
