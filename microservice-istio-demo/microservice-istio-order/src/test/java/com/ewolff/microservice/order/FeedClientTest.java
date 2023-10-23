package com.ewolff.microservice.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.ewolff.microservice.order.customer.CustomerRepository;
import com.ewolff.microservice.order.logic.Order;
import com.ewolff.microservice.order.logic.OrderRepository;

@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class FeedClientTest {

	@LocalServerPort
	private long serverPort;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	void feedReturnsBasicInformation() {
		OrderFeed feed = retrieveFeed();
		assertNotNull(feed.getUpdated());
	}

	@Test
	void requestWithLastModifiedReturns304() {
		ResponseEntity<OrderFeed> response = restTemplate.exchange(feedUrl(), HttpMethod.GET, new HttpEntity(null),
				OrderFeed.class);

		ZonedDateTime lastModified = response.getHeaders().getFirstZonedDateTime("Last-Modified");

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setZonedDateTime("If-Modified-Since", lastModified);
		HttpEntity requestEntity = new HttpEntity(requestHeaders);

		response = restTemplate.exchange(feedUrl(), HttpMethod.GET, requestEntity, OrderFeed.class);

		assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
	}

	@Test
	void feedReturnsNewlyCreatedOrder() {
		Order order = new Order();
		order.setCustomer(customerRepository.findAll(Sort.unsorted()).iterator().next());
		orderRepository.save(order);
		OrderFeed feed = retrieveFeed();
		boolean foundLinkToCreatedOrder = false;
		for (OrderFeedEntry entry : feed.getOrders()) {
			if (entry.getLink().contains(Long.toString(order.getId()))) {
				foundLinkToCreatedOrder = true;
			}
		}
		assertTrue(foundLinkToCreatedOrder);
	}

	private OrderFeed retrieveFeed() {
		return restTemplate.getForEntity(feedUrl(), OrderFeed.class).getBody();
	}

	private String feedUrl() {
		return "http://localhost:%d/feed".formatted(serverPort);
	}

}
