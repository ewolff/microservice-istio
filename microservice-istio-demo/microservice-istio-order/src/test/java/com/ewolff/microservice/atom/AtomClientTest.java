package com.ewolff.microservice.atom;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.ewolff.microservice.order.OrderApp;
import com.ewolff.microservice.order.customer.CustomerRepository;
import com.ewolff.microservice.order.logic.Order;
import com.ewolff.microservice.order.logic.OrderRepository;
import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AtomClientTest {

	@LocalServerPort
	private long serverPort;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void feedReturnsBasicInformation() {
		Feed feed = retrieveFeed();
		assertEquals("Order", feed.getTitle());
	}

	@Test
	public void requestWithLastModifiedReturns304() {
		Order order = new Order();
		order.setCustomer(customerRepository.findAll().iterator().next());
		orderRepository.save(order);
		ResponseEntity<Feed> response = restTemplate.exchange(feedUrl(), HttpMethod.GET, new HttpEntity(null),
				Feed.class);

		Date lastModified = DateUtils.parseDate(response.getHeaders().getFirst("Last-Modified"));

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set("If-Modified-Since", DateUtils.formatDate(lastModified));
		HttpEntity requestEntity = new HttpEntity(requestHeaders);

		response = restTemplate.exchange(feedUrl(), HttpMethod.GET, requestEntity, Feed.class);

		assertEquals(HttpStatus.NOT_MODIFIED, response.getStatusCode());
	}

	@Test
	public void feedReturnsNewlyCreatedOrder() {
		Order order = new Order();
		order.setCustomer(customerRepository.findAll().iterator().next());
		orderRepository.save(order);
		Feed feed = retrieveFeed();
		boolean foundLinkToCreatedOrder = false;
		List<Entry> entries = feed.getEntries();
		for (Entry entry : entries) {
			for (Content content : entry.getContents()) {
				if (content.getSrc().contains(Long.toString(order.getId()))) {
					foundLinkToCreatedOrder = true;
				}
			}
		}
		assertTrue(foundLinkToCreatedOrder);
	}

	private Feed retrieveFeed() {
		for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
			System.out.println(converter);
		}
		Feed feed = restTemplate.getForEntity(feedUrl(), Feed.class).getBody();
		return feed;
	}

	private String feedUrl() {
		return String.format("http://localhost:%d/feed", serverPort);
	}

}
