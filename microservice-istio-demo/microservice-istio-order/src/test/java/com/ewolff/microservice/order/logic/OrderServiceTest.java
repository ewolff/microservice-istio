package com.ewolff.microservice.order.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ewolff.microservice.order.OrderApp;

@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class OrderServiceTest {

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@Transactional
	void lastCreatedIsUpdated() {
		Order order = new Order();
		order = orderRepository.save(order);
		assertEquals(order.getUpdated(), orderRepository.lastUpdate());
		order = new Order();
		order = orderRepository.save(order);
		assertEquals(order.getUpdated(), orderRepository.lastUpdate());
	}

}
