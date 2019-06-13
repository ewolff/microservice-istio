package com.ewolff.microservice.order.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerTestDataGenerator {

	private final CustomerRepository customerRepository;

	@Autowired
	public CustomerTestDataGenerator(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	private void createIfNotExist(String firstname, String name, String email, String street, String city) {
		if (customerRepository.findByName(name).size() == 0) {
			customerRepository.save(new Customer(firstname, name, email, street, city));
		}
	}

	@PostConstruct
	public void generateTestData() {
		createIfNotExist("Eberhard", "Wolff", "eberhard.wolff@posteo.net", "Unter den Linden", "Berlin");
		createIfNotExist("Rod", "Johnson", "rod@somewhere.com", "Market Street", "San Francisco");
	}

}
