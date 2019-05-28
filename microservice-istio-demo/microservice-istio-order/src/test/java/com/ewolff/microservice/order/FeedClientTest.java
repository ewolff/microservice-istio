package com.ewolff.microservice.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.http.client.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import com.ewolff.microservice.order.OrderApp;
import com.ewolff.microservice.order.OrderFeed;
import com.ewolff.microservice.order.OrderFeedEntry;
import com.ewolff.microservice.order.customer.CustomerRepository;
import com.ewolff.microservice.order.logic.Order;
import com.ewolff.microservice.order.logic.OrderRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class FeedClientTest {

	@LocalServerPort
	private long serverPort;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void feedReturnsBasicInformation() {
		OrderFeed feed = retrieveFeed();
		assertNotNull(feed.getUpdated());
	}

	@Test
	public void requestWithLastModifiedReturns304() {
		ResponseEntity<OrderFeed> response = restTemplate.exchange(feedUrl(), HttpMethod.GET, new HttpEntity(null),
				OrderFeed.class);

		Date lastModified = DateUtils.parseDate(response.getHeaders().getFirst("Last-Modified"));

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("If-Modified-Since", DateUtils.formatDate(lastModified));
		HttpEntity requestEntity = new HttpEntity(requestHeaders);

		response = restTemplate.exchange(feedUrl(), HttpMethod.GET, requestEntity, OrderFeed.class);

		assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
	}

	@Test
	public void feedReturnsNewlyCreatedOrder() {
		Order order = new Order();
		order.setCustomer(customerRepository.findAll().iterator().next());
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
		return String.format("http://localhost:%d/feed", serverPort);
	}

}
