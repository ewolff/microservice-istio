package com.ewolff.microservice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.TypeHint;
import org.thymeleaf.engine.IterationStatusVar;

import com.ewolff.microservice.order.customer.Customer;
import com.ewolff.microservice.order.item.Item;
import com.ewolff.microservice.order.logic.Address;
import com.ewolff.microservice.order.logic.Order;
import com.ewolff.microservice.order.logic.OrderLine;

@SpringBootApplication
@TypeHint(types = { Customer.class, Item.class, Order.class, Address.class, OrderLine.class }, access = AccessBits.ALL)
@TypeHint(types = { IterationStatusVar.class }, access = AccessBits.ALL)
public class OrderApp {

	public static void main(String[] args) {
		SpringApplication.run(OrderApp.class, args);
	}

}
