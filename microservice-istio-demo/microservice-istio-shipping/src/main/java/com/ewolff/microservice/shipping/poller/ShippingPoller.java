package com.ewolff.microservice.shipping.poller;

import java.util.Date;

import com.ewolff.microservice.shipping.Shipment;
import com.ewolff.microservice.shipping.ShipmentService;

import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ShippingPoller {

	private final Logger log = LoggerFactory.getLogger(ShippingPoller.class);

	private String url = "";

	private RestTemplate restTemplate = new RestTemplate();

	private Date lastModified = null;

	private ShipmentService shipmentService;

	private boolean pollingActivated = true;

	@Autowired
	public ShippingPoller(@Value("${order.url}") String url, @Value("${poller.actived:true}") boolean pollingActivated,
			ShipmentService shipmentService) {
		super();
		this.url = url;
		this.shipmentService = shipmentService;
		this.pollingActivated = pollingActivated;
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
			requestHeaders.set(HttpHeaders.IF_MODIFIED_SINCE, DateUtils.formatDate(lastModified));
		}
		HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
		ResponseEntity<OrderFeed> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, OrderFeed.class);

		if (response.getStatusCode() != HttpStatus.NOT_MODIFIED) {
			log.trace("data has been modified");
			OrderFeed feed = response.getBody();
			for (OrderFeedEntry entry : feed.getOrders()) {
				if ((lastModified == null) || (entry.getUpdated().after(lastModified))) {
					Shipment shipping = restTemplate
							.getForEntity(entry.getLink(), Shipment.class).getBody();
					log.trace("saving shipping {}", shipping.getId());
					shipmentService.ship(shipping);
				}
			}
			if (response.getHeaders().getFirst("Last-Modified") != null) {
				lastModified = DateUtils.parseDate(response.getHeaders().getFirst(HttpHeaders.LAST_MODIFIED));
				log.trace("Last-Modified header {}", lastModified);
			}
		} else {
			log.trace("no new data");
		}
	}

}
