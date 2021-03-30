package com.ewolff.microservice.bonus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.ProxyHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.engine.IterationStatusVar;

import com.ewolff.microservice.bonus.poller.OrderFeed;
import com.ewolff.microservice.bonus.poller.OrderFeedEntry;

@EnableScheduling
@SpringBootApplication
@TypeHint(types = { Customer.class, Bonus.class, OrderFeed.class, OrderFeedEntry.class }, access = AccessBits.ALL)
@TypeHint(types = { IterationStatusVar.class }, access = AccessBits.ALL)
@ProxyHint(types = BonusService.class)
public class BonusApp {

	public static void main(String[] args) {
		SpringApplication.run(BonusApp.class, args);
	}

}
