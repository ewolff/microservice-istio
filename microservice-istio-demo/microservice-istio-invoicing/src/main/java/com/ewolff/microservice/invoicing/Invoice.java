package com.ewolff.microservice.invoicing;

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
public class Invoice {

	@Id
	private long id;

	@Embedded
	private Customer customer;

	private Date updated;

	@Embedded
	private Address billingAddress = new Address();

	@OneToMany(cascade = CascadeType.ALL)
	private List<InvoiceLine> invoiceLine;

	public Invoice() {
		super();
		invoiceLine = new ArrayList<InvoiceLine>();
	}

	public Invoice(long id, Customer customer, Date updated, Address billingAddress, List<InvoiceLine> invoiceLine) {
		super();
		this.id = id;
		this.customer = customer;
		this.updated = updated;
		this.billingAddress = billingAddress;
		this.invoiceLine = invoiceLine;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
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
	}

	public List<InvoiceLine> getInvoiceLine() {
		return invoiceLine;
	}

	public void setInvoiceLine(List<InvoiceLine> invoiceLine) {
		this.invoiceLine = invoiceLine;
	}

	@Transient
	public void setOrderLine(List<InvoiceLine> orderLine) {
		this.invoiceLine = orderLine;
	}

	public void addLine(int count, Item item) {
		this.invoiceLine.add(new InvoiceLine(count, item));
	}

	public int getNumberOfLines() {
		return invoiceLine.size();
	}

	public double totalAmount() {
		return invoiceLine.stream().map((ol) -> ol.getCount() * ol.getItem().getPrice()).reduce(0.0,
				(d1, d2) -> d1 + d2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((billingAddress == null) ? 0 : billingAddress.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((invoiceLine == null) ? 0 : invoiceLine.hashCode());
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
		Invoice other = (Invoice) obj;
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
		if (id != other.id)
			return false;
		if (invoiceLine == null) {
			if (other.invoiceLine != null)
				return false;
		} else if (!invoiceLine.equals(other.invoiceLine))
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
		return "Invoice [id=" + id + ", customer=" + customer + ", updated=" + updated + ", billingAddress="
				+ billingAddress + ", invoiceLine=" + invoiceLine + "]";
	}

}
