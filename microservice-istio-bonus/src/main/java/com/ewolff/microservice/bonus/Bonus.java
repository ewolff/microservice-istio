package com.ewolff.microservice.bonus;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Bonus {

	@Id
	private long id;

	@Embedded
	private Customer customer;

	private Date updated;

	private double revenue;
	
	public Bonus() {
		super();
	}

	public Bonus(long id, Customer customer, Date updated, double revenue) {
		super();
		this.id = id;
		this.customer = customer;
		this.updated = updated;
		this.revenue = revenue;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
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

	public Bonus(Customer customer) {
		super();
		this.customer = customer;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
