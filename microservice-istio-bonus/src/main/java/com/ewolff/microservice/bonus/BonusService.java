package com.ewolff.microservice.bonus;

import org.springframework.transaction.annotation.Transactional;

public interface BonusService {

	void calculateBonus(Bonus bonus);

}