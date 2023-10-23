package com.ewolff.microservice.shipping;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = ShippingTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class ShippingWebIntegrationTest {

	@LocalServerPort
	private int serverPort;

	private RestTemplate restTemplate = new RestTemplate();

	@Test
	void isHTMLReturned() {
		String body = getForMediaType(String.class, MediaType.TEXT_HTML, shippingURL());

		assertThat(body, containsString("<div"));
	}

	private String shippingURL() {
		return "http://localhost:" + serverPort;
	}

	private <T> T getForMediaType(Class<T> value, MediaType mediaType, String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(mediaType));

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<T> resultEntity = restTemplate.exchange(url, HttpMethod.GET, entity, value);

		return resultEntity.getBody();
	}

}
