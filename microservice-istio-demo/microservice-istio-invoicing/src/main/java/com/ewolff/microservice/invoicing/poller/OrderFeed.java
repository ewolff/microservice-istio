package com.ewolff.microservice.invoicing.poller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderFeed {

    private Date updated;

    private List<OrderFeedEntry> orders;

    public OrderFeed() {
        super();
        orders = new ArrayList<OrderFeedEntry>();
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public List<OrderFeedEntry> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderFeedEntry> orders) {
        this.orders = orders;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orders == null) ? 0 : orders.hashCode());
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
		OrderFeed other = (OrderFeed) obj;
		if (orders == null) {
			if (other.orders != null)
				return false;
		} else if (!orders.equals(other.orders))
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
		return "OrderFeed [updated=" + updated + ", orders=" + orders + "]";
	}

    
    
}