package com.ewolff.microservice.shipping;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShippingTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ShippingServiceTest {

	@Autowired
	private ShipmentRepository shipmentRepository;

	@Autowired
	private ShipmentService shipmentService;

	@Test
	public void ensureIdempotencySecondCallIgnored() {
		long countBefore = shipmentRepository.count();
		Shipment shipment = new Shipment(42L,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(0L), new Address("Krischstr. 100", "40789", "Monheim am Rhein"),
				new ArrayList<ShipmentLine>(), "DHL");
		shipmentService.ship(shipment);
		assertThat(shipmentRepository.count(), is(countBefore + 1));
		assertThat(shipmentRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
		shipment = new Shipment(42,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(), new Address("Krischstr. 100", "40789", "Monheim am Rhein"), new ArrayList<ShipmentLine>(),
				"DHL");
		shipmentService.ship(shipment);
		assertThat(shipmentRepository.count(), is(countBefore + 1));
		assertThat(shipmentRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
	}

	@Test
	public void ensureShipmentRateCalculted() {
		Shipment shipment = new Shipment(43L,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(0L), new Address("Krischstr. 100", "40789", "Monheim am Rhein"),
				new ArrayList<ShipmentLine>(), "DHL");
		shipmentService.ship(shipment);
		assertThat(shipment.getCost(), is(1));
	}

	@Test
	public void ensureUnkownShipmentError() {
		Shipment shipment = new Shipment(44L,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(0L), new Address("Krischstr. 100", "40789", "Monheim am Rhein"),
				new ArrayList<ShipmentLine>(), "Unkown Service");
		assertThrows(IllegalArgumentException.class, () -> shipmentService.ship(shipment));
	}

}
