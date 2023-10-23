package com.ewolff.microservice.invoicing.poller;

import java.time.ZonedDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ewolff.microservice.invoicing.Invoice;
import com.ewolff.microservice.invoicing.InvoiceService;

@Component
public class InvoicePoller {

	private final Logger log = LoggerFactory.getLogger(InvoicePoller.class);

	private String url = "";

	private RestTemplate restTemplate = new RestTemplate();

	private ZonedDateTime lastModified = null;

	private boolean pollingActivated;

	private InvoiceService invoiceService;
	
	public InvoicePoller(@Value("${order.url}") String url, @Value("${poller.actived:true}") boolean pollingActivated,
			InvoiceService invoiceService) {
		super();
		this.pollingActivated = pollingActivated;
		this.url = url;
		this.invoiceService = invoiceService;
	}

	@Scheduled(fixedDelay = 30000)
	public void poll() {
		if (pollingActivated) {
			pollInternal();
		}
	}

	public void pollInternal() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.set(HttpHeaders.ACCEPT, "*/*");
		if (lastModified != null) {
			requestHeaders.setZonedDateTime(HttpHeaders.IF_MODIFIED_SINCE, lastModified);
		}
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<OrderFeed> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, OrderFeed.class);

		if (response.getStatusCode() != HttpStatus.NOT_MODIFIED) {
			log.trace("data has been modified");
			OrderFeed feed = response.getBody();
			for (OrderFeedEntry entry : feed.getOrders()) {
				if ((lastModified == null) || (entry.getUpdated().after(Date.from(lastModified.toInstant())))) {
					Invoice invoice = restTemplate
							.getForEntity(entry.getLink(), Invoice.class).getBody();
					log.trace("saving invoice {}", invoice.getId());
					invoiceService.generateInvoice(invoice);
				}
			}
			if (response.getHeaders().getFirst(HttpHeaders.LAST_MODIFIED) != null) {
				lastModified = response.getHeaders().getFirstZonedDateTime(HttpHeaders.LAST_MODIFIED);
				log.trace("Last-Modified header {}", lastModified);
			}
		} else {
			log.trace("no new data");
		}
	}

}
