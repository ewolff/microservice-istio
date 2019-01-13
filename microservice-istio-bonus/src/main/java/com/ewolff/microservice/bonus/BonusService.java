package com.ewolff.microservice.bonus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BonusService {

	private final Logger log = LoggerFactory.getLogger(BonusService.class);

	private BonusRepository bonusRepository;

	@Autowired
	public BonusService(BonusRepository bonusRepository) {
		super();
		this.bonusRepository = bonusRepository;
	}

	@Transactional
	public void calculateBonus(Bonus bonus) {
		if (bonusRepository.existsById(bonus.getId())) {
			log.info("Bonus id {} already exists - ignored", bonus.getId());
		} else {
			bonusRepository.save(bonus);
		}
	}

}
