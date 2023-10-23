package com.ewolff.microservice.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.ewolff.microservice.order.item.ItemRepository;
import com.ewolff.microservice.order.item.ItemTestDataGenerator;

@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class ItemTestDataGeneratorTest {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemTestDataGenerator itemTestDataGenerator;

	@Test
	void assureTestDataGeneratedOnce() {
		assertEquals(1, itemRepository.findByName("iPod").size());
		itemTestDataGenerator.generateTestData();
		assertEquals(1, itemRepository.findByName("iPod").size());
	}

}
