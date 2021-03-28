package com.ewolff.microservice.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.ProxyHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.engine.IterationStatusVar;

import com.ewolff.microservice.shipping.poller.OrderFeed;
import com.ewolff.microservice.shipping.poller.OrderFeedEntry;

@EnableScheduling
@SpringBootApplication
@TypeHint(types = { Customer.class, Item.class, Shipment.class, Address.class, ShipmentLine.class, OrderFeed.class,
		OrderFeedEntry.class }, access = AccessBits.ALL)
@TypeHint(types = { IterationStatusVar.class }, access = AccessBits.ALL)
@ProxyHint(types = ShipmentService.class)
public class ShippingApp {

	public static void main(String[] args) {
		SpringApplication.run(ShippingApp.class, args);
	}

}
