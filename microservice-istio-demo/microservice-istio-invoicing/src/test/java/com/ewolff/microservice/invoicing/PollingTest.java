package com.ewolff.microservice.invoicing;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ewolff.microservice.invoicing.poller.InvoicePoller;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = InvoiceTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PollingTest {

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private InvoicePoller invoicePoller;

	@Test
	public void orderArePolled() {
		long countBeforePoll = invoiceRepository.count();
		invoicePoller.pollInternal();
		assertThat(invoiceRepository.count(), is(greaterThan(countBeforePoll)));
	}

}
