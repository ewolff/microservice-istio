package com.ewolff.microservice.shipping;

import org.springframework.transaction.annotation.Transactional;

public interface ShipmentService {

	void ship(Shipment shipment);

}