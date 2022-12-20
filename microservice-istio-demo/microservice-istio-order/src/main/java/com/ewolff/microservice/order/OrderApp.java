package com.ewolff.microservice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import com.ewolff.microservice.order.customer.CustomerTestDataGenerator;
import com.ewolff.microservice.order.item.ItemTestDataGenerator;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@ImportRuntimeHints(NativeRuntimeHints.class)
public class OrderApp {
	
	private CustomerTestDataGenerator customerTestDataGenerator;
	private ItemTestDataGenerator itemTestDataGenerator;
	
	public OrderApp(CustomerTestDataGenerator customerTestDataGenerator, ItemTestDataGenerator itemTestDataGenerator) {
		super();
		this.customerTestDataGenerator = customerTestDataGenerator;
		this.itemTestDataGenerator = itemTestDataGenerator;
	}

	@PostConstruct
	public void generateTestData() {
		customerTestDataGenerator.generateTestData();
		itemTestDataGenerator.generateTestData();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(OrderApp.class, args);
	}

}
