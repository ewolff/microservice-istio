package com.ewolff.microservice.invoicing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.engine.IterationStatusVar;

import com.ewolff.microservice.invoicing.poller.OrderFeed;
import com.ewolff.microservice.invoicing.poller.OrderFeedEntry;

@EnableScheduling
@SpringBootApplication
@TypeHint(types = { OrderFeed.class, OrderFeedEntry.class }, access = AccessBits.ALL)
@TypeHint(types = { IterationStatusVar.class }, access = AccessBits.ALL)
public class InvoiceApp {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceApp.class, args);
	}

}
