package com.ewolff.microservice.order.logic;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, CrudRepository<Order, Long>  {

	@Query("SELECT max(o.updated) FROM Order o")
	Date lastUpdate();

}
