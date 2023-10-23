package com.ewolff.microservice.bonus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BonusTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class BonusServiceTest {

	@Autowired
	private BonusRepository bonusRepository;

	@Autowired
	private BonusService bonusService;

	@Test
	void ensureIdempotencySecondCallIgnored() {
		long countBefore = bonusRepository.count();
		Bonus bonus = new Bonus(42L,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(0L), 42.0);
		bonusService.calculateBonus(bonus);
		assertThat(bonusRepository.count(), is(countBefore + 1));
		assertThat(bonusRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
		bonus = new Bonus(42,
				new Customer(23L, "Eberhard", "Wolff"),
				new Date(), 42.0d);
		bonusService.calculateBonus(bonus);
		assertThat(bonusRepository.count(), is(countBefore + 1));
		assertThat(bonusRepository.findById(42L).get().getUpdated().getTime(), equalTo(0L));
	}

}
