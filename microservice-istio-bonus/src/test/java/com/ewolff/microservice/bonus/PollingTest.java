package com.ewolff.microservice.bonus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import com.ewolff.microservice.bonus.poller.BonusPoller;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(classes = BonusTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@PactTestFor(providerName = "OrderProvider", port = "8081")
@ActiveProfiles("test")
public class PollingTest {

	@Autowired
	private BonusRepository bonusRepository;

	@Autowired
	private BonusPoller bonusPoller;

	private DslPart feedBody(Date now) {
		return new PactDslJsonBody().date("updated", "yyyy-MM-dd'T'kk:mm:ss.SSS+0000", now)
									.eachLike("orders")
									.numberType("id", 1)
									.stringType("link", "http://localhost:8081/order/1")
									.date("updated", "yyyy-MM-dd'T'kk:mm:ss.SSS+0000", now)
									.closeArray();
	}

	public DslPart order(Date now) {
		return new PactDslJsonBody()
									.numberType("id", 1)
									.numberType("numberOfLines", 1)
									.numberType("revenue", 42)
									.object("customer")
									.numberType("customerId", 1)
									.stringType("name", "Wolff")
									.stringType("firstname", "Eberhard")
									.stringType("email", "eberhard.wolff@posteo.net")
									.closeObject();
	}

	@Pact(consumer = "Bonus")
	public V4Pact createFragment(PactDslWithProvider builder) {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		Date now = new Date();
		return builder	.uponReceiving("Request for order feed")
						.method("GET")
						.path("/feed")
						.willRespondWith()
						.status(200)
						.headers(headers)
						.body(feedBody(now))
						.uponReceiving("Request for an order")
						.method("GET")
						.path("/order/1")
						.willRespondWith()
						.status(200)
						.headers(headers)
						.body(order(now))
						.toPact(V4Pact.class);
	}

	@Test
	void orderArePolled() {
		long countBeforePoll = bonusRepository.count();
		bonusPoller.pollInternal();
		assertThat(bonusRepository.count(), is(greaterThan(countBeforePoll)));
		for (Bonus bonus : bonusRepository.findAll()) {
			assertThat(bonus.getRevenue(), is(greaterThan(0.0)));
		}
	}

}
