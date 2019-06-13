package com.ewolff.microservice.order.item;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemTestDataGenerator {

	private final ItemRepository itemRepository;

	@Autowired
	public ItemTestDataGenerator(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@PostConstruct
	public void generateTestData() {
		createIfNotExist("iPod", 42.0);
		createIfNotExist("iPod touch", 21.0);
		createIfNotExist("iPod nano", 1.0);
		createIfNotExist("Apple TV", 100.0);
	}

	private void createIfNotExist(String name, double price) {
		if (itemRepository.findByName(name).size() == 0) {
			itemRepository.save(new Item(name, price));
		}
	}

}
