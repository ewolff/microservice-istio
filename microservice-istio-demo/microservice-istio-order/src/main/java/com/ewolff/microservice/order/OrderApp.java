package com.ewolff.microservice.order;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;

import com.ewolff.microservice.order.customer.CustomerTestDataGenerator;
import com.ewolff.microservice.order.item.ItemTestDataGenerator;

@SpringBootApplication
@TypeHint(types =  ArrayList.class)
public class OrderApp {
	
	private CustomerTestDataGenerator customerTestDataGenerator;
	private ItemTestDataGenerator itemTestDataGenerator;
	
	@Autowired
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
