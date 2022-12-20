package com.ewolff.microservice.bonus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BonusServiceImpl implements BonusService {

	private final Logger log = LoggerFactory.getLogger(BonusServiceImpl.class);

	private BonusRepository bonusRepository;

	public BonusServiceImpl(BonusRepository bonusRepository) {
		super();
		this.bonusRepository = bonusRepository;
	}

	@Override
	@Transactional
	public void calculateBonus(Bonus bonus) {
		if (bonusRepository.existsById(bonus.getId())) {
			log.info("Bonus id {} already exists - ignored", bonus.getId());
		} else {
			bonusRepository.save(bonus);
		}
	}

}
