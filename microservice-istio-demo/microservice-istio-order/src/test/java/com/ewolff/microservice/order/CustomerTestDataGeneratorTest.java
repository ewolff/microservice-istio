package com.ewolff.microservice.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ewolff.microservice.order.customer.CustomerRepository;
import com.ewolff.microservice.order.customer.CustomerTestDataGenerator;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class CustomerTestDataGeneratorTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerTestDataGenerator customerTestDataGenerator;

	@Test
	public void assureTestDataGeneratedOnce() {
		assertEquals(1, customerRepository.findByName("Wolff").size());
		customerTestDataGenerator.generateTestData();
		assertEquals(1, customerRepository.findByName("Wolff").size());
	}

}
