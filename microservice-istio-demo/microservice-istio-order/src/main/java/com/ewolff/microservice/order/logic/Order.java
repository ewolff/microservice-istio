package com.ewolff.microservice.order.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ewolff.microservice.order.customer.Customer;
import com.ewolff.microservice.order.item.Item;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDERTABLE")
public class Order {

	@Id
	private long id;

	@ManyToOne
	private Customer customer;

	private String deliveryService;

	private Date updated;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "street", column = @Column(name = "SHIPPING_STREET")),
			@AttributeOverride(name = "zip", column = @Column(name = "SHIPPING_ZIP")),
			@AttributeOverride(name = "city", column = @Column(name = "SHIPPING_CITY")) })
	private Address shippingAddress = new Address();

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET")),
			@AttributeOverride(name = "zip", column = @Column(name = "BILLING_ZIP")),
			@AttributeOverride(name = "city", column = @Column(name = "BILLING_CITY")) })
	private Address billingAddress = new Address();

	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderLine> orderLine;

	private static long generateID() {
		UUID uuid = UUID.randomUUID();
		return Math.abs(uuid.getLeastSignificantBits());
	}

	public Order() {
		this(generateID());
	}

	public Order(long id) {
		super();
		this.id = id;
		orderLine = new ArrayList<OrderLine>();
		updated();
	}

	public Order(Customer customer) {
		this(customer,generateID());
	}

	public Order(Customer customer, long id) {
		super();
		this.customer = customer;
		this.orderLine = new ArrayList<OrderLine>();
		this.id = id;
		updated();
	}

	private void updated() {
		updated = new Date();
	}

	public String getDeliveryService() {
		return deliveryService;
	}

	public void setDeliveryService(String deliveryService) {
		this.deliveryService = deliveryService;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		updated();
		this.shippingAddress = shippingAddress;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		updated();
		this.billingAddress = billingAddress;
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

	public void setCustomer(Customer customer) {
		this.customer = customer;
		updated();
	}

	public List<OrderLine> getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(List<OrderLine> orderLine) {
		this.orderLine = orderLine;
		updated();
	}

	public void addLine(int count, Item item) {
		this.orderLine.add(new OrderLine(count, item));
		updated();
	}

	public int getNumberOfLines() {
		return orderLine.size();
	}

	public double totalPrice() {
		return orderLine.stream().map((ol) -> ol.getCount() * ol.getItem().getPrice()).reduce(0.0, (d1, d2) -> d1 + d2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((billingAddress == null) ? 0 : billingAddress.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((deliveryService == null) ? 0 : deliveryService.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((orderLine == null) ? 0 : orderLine.hashCode());
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
		Order other = (Order) obj;
		if (billingAddress == null) {
			if (other.billingAddress != null)
				return false;
		} else if (!billingAddress.equals(other.billingAddress))
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
		if (orderLine == null) {
			if (other.orderLine != null)
				return false;
		} else if (!orderLine.equals(other.orderLine))
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
		return "Order [id=" + id + ", customer=" + customer + ", deliveryService=" + deliveryService + ", updated="
				+ updated + ", shippingAddress=" + shippingAddress + ", billingAddress=" + billingAddress
				+ ", orderLine=" + orderLine + "]";
	}



}
