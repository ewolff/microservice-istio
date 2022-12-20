package com.ewolff.microservice.bonus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ImportRuntimeHints(NativeRuntimeHints.class)
public class BonusApp {

	public static void main(String[] args) {
		SpringApplication.run(BonusApp.class, args);
	}

}