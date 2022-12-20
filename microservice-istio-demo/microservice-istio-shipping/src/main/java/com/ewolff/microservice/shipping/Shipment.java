package com.ewolff.microservice.shipping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Shipment {

	@Id
	private long id;

	@Embedded
	private Customer customer;

	private Date updated;

	@Embedded
	private Address shippingAddress = new Address();

	@OneToMany(cascade = CascadeType.ALL)
	private List<ShipmentLine> shipmentLine;

	private String deliveryService;

	private int cost;

	public Shipment() {
		super();
		shipmentLine = new ArrayList<ShipmentLine>();
	}

	public Shipment(long id, Customer customer, Date updated, Address shippingAddress, List<ShipmentLine> shipmentLine,
			String deliveryService) {
		super();
		this.id = id;
		this.customer = customer;
		this.updated = updated;
		this.shippingAddress = shippingAddress;
		this.shipmentLine = shipmentLine;
		this.deliveryService = deliveryService;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(String deliveryService) {
		this.deliveryService = deliveryService;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date created) {
		this.updated = created;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customerId) {
		this.customer = customerId;
	}

	public List<ShipmentLine> getShipmentLine() {
		return shipmentLine;
	}

	public Shipment(Customer customer) {
		super();
		this.customer = customer;
		this.shipmentLine = new ArrayList<ShipmentLine>();
	}

	public void setShipmentLine(List<ShipmentLine> shipmentLine) {
		this.shipmentLine = shipmentLine;
	}

	@Transient
	public void setOrderLine(List<ShipmentLine> orderLine) {
		this.shipmentLine = orderLine;
	}

	public int getNumberOfLines() {
		return shipmentLine.size();
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

	public void calculateShippingCost() {
		if (getDeliveryService().equalsIgnoreCase("DHL")) {
			setCost(1);
		} else if (getDeliveryService().equalsIgnoreCase("Hermes")) {
			setCost(2);
		} else {
			throw new IllegalArgumentException("Unknown Delivery Service!");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cost;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((deliveryService == null) ? 0 : deliveryService.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((shipmentLine == null) ? 0 : shipmentLine.hashCode());
		result = prime * result + ((shippingAddress == null) ? 0 : shippingAddress.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shipment other = (Shipment) obj;
		if (cost != other.cost)
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (deliveryService == null) {
			if (other.deliveryService != null)
				return false;
		} else if (!deliveryService.equals(other.deliveryService))
			return false;
		if (id != other.id)
			return false;
		if (shipmentLine == null) {
			if (other.shipmentLine != null)
				return false;
		} else if (!shipmentLine.equals(other.shipmentLine))
			return false;
		if (shippingAddress == null) {
			if (other.shippingAddress != null)
				return false;
		} else if (!shippingAddress.equals(other.shippingAddress))
			return false;
		if (updated == null) {
			if (other.updated != null)
				return false;
		} else if (!updated.equals(other.updated))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Shipment [id=" + id + ", customer=" + customer + ", updated=" + updated + ", shippingAddress="
				+ shippingAddress + ", shipmentLine=" + shipmentLine + ", deliveryService=" + deliveryService
				+ ", cost=" + cost + "]";
	}
	
	
}
