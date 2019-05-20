package com.ewolff.microservice.order.logic;

import com.ewolff.microservice.order.customer.Customer;
import com.ewolff.microservice.order.item.Item;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
class SpringRestDataConfig implements RepositoryRestConfigurer {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Order.class, Item.class, Customer.class);
		config.setDefaultMediaType(MediaType.APPLICATION_JSON);
	}

}
