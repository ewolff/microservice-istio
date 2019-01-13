package com.ewolff.microservice.shipping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ewolff.microservice.shipping.poller.ShippingPoller;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShippingTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PollingTest {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Autowired
	private ShippingPoller shippingPoller;

	@Test
	public void orderArePolled() {
		long countBeforePoll = shipmentRepository.count();
		shippingPoller.pollInternal();
		assertThat(shipmentRepository.count(), is(greaterThan(countBeforePoll)));
	}

}
