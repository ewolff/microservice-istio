package com.ewolff.microservice.bonus;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BonusRepository extends PagingAndSortingRepository<Bonus, Long>, CrudRepository<Bonus, Long> {

	@Query("SELECT max(b.updated) FROM Bonus b")
	Date lastUpdate();

}
