package com.ewolff.microservice.bonus;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ewolff.microservice.bonus.BonusRepository;
import com.ewolff.microservice.bonus.poller.BonusPoller;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BonusTestApp.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PollingTest {

	@Autowired
	private BonusRepository bonusRepository;

	@Autowired
	private BonusPoller bonusPoller;

	@Test
	public void orderArePolled() {
		long countBeforePoll = bonusRepository.count();
		bonusPoller.pollInternal();
		assertThat(bonusRepository.count(), is(greaterThan(countBeforePoll)));
	}

}
