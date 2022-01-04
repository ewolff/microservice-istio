package com.ewolff.microservice.bonus;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ewolff.microservice.bonus.poller.OrderFeed;
import com.ewolff.microservice.bonus.poller.OrderFeedEntry;

@EnableScheduling
@SpringBootApplication
@TypeHint(types = { OrderFeed.class, OrderFeedEntry.class, ArrayList.class }, access = { TypeAccess.PUBLIC_CONSTRUCTORS,
		TypeAccess.DECLARED_CONSTRUCTORS, TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_CLASSES,
		TypeAccess.DECLARED_METHODS, TypeAccess.PUBLIC_FIELDS, TypeAccess.PUBLIC_METHODS,
		TypeAccess.QUERY_DECLARED_CONSTRUCTORS, TypeAccess.QUERY_PUBLIC_METHODS })
public class BonusApp {

	public static void main(String[] args) {
		SpringApplication.run(BonusApp.class, args);
	}

}