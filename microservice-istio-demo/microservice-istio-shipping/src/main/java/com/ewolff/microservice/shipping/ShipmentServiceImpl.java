package com.ewolff.microservice.shipping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentServiceImpl implements ShipmentService  {

	private final Logger log = LoggerFactory.getLogger(ShipmentServiceImpl.class);

	private ShipmentRepository shipmentRepository;

	public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
		super();
		this.shipmentRepository = shipmentRepository;
	}

	@Override
	@Transactional
	public void ship(Shipment shipment) {
		if (shipmentRepository.existsById(shipment.getId())) {
			log.info("Shipment id {} already exists - ignored", shipment.getId());
		} else {
			shipment.calculateShippingCost();
			shipmentRepository.save(shipment);
		}
	}

}
