package com.ewolff.microservice.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.ewolff.microservice.order.customer.CustomerRepository;
import com.ewolff.microservice.order.customer.CustomerTestDataGenerator;

@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class CustomerTestDataGeneratorTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerTestDataGenerator customerTestDataGenerator;

	@Test
	void assureTestDataGeneratedOnce() {
		assertEquals(1, customerRepository.findByName("Wolff").size());
		customerTestDataGenerator.generateTestData();
		assertEquals(1, customerRepository.findByName("Wolff").size());
	}

}
