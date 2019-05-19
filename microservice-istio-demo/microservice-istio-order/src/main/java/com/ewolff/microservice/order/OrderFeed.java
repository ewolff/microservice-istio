package com.ewolff.microservice.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import net.logstash.logback.encoder.org.apache.commons.lang.builder.HashCodeBuilder;

public class OrderFeed {

    private Date updated;

    private List<OrderFeedEntry> orders;

    public OrderFeed() {
        super();
    }

    public OrderFeed(Date updated) {
        super();
        this.updated = updated;
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