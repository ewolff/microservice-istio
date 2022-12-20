package com.ewolff.microservice.bonus;

import java.util.Date;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		long temp;
		temp = Double.doubleToLongBits(revenue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Bonus other = (Bonus) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(revenue) != Double.doubleToLongBits(other.revenue))
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
		return "Bonus [id=" + id + ", customer=" + customer + ", updated=" + updated + ", revenue=" + revenue + "]";
	}


}
