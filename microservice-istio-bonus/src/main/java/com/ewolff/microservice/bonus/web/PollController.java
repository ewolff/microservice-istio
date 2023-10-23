package com.ewolff.microservice.bonus.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.ewolff.microservice.bonus.poller.BonusPoller;

@Controller
public class PollController {

	private BonusPoller poller;

	public PollController(BonusPoller poller) {
		this.poller = poller;
	}

	@PostMapping("/poll")
	public String poll() {
		poller.poll();
		return "redirect:/";
	}

}
