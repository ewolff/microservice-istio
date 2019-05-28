package com.ewolff.microservice.order.logic;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.ewolff.microservice.order.OrderApp;
import com.ewolff.microservice.order.logic.Order;
import com.ewolff.microservice.order.logic.OrderRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class OrderServiceTest {

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@Transactional
	public void lastCreatedIsUpdated() {
		Order order = new Order();
		order = orderRepository.save(order);
		assertEquals(order.getUpdated(), orderRepository.lastUpdate());
		order = new Order();
		order = orderRepository.save(order);
		assertEquals(order.getUpdated(), orderRepository.lastUpdate());
	}

}
