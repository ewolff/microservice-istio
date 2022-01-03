package com.ewolff.microservice.shipping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.engine.IterationStatusVar;

import com.ewolff.microservice.shipping.poller.OrderFeed;
import com.ewolff.microservice.shipping.poller.OrderFeedEntry;

@EnableScheduling
@SpringBootApplication
@TypeHint(types = { OrderFeed.class, OrderFeedEntry.class })
@TypeHint(types = { IterationStatusVar.class })
public class ShippingApp {

	public static void main(String[] args) {
		SpringApplication.run(ShippingApp.class, args);
	}

}
