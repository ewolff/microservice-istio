package com.ewolff.microservice.bonus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ewolff.microservice.bonus.poller.BonusPoller;

@Controller
public class PollController {

	private BonusPoller poller;

	@Autowired
	public PollController(BonusPoller poller) {
		this.poller = poller;
	}

	@RequestMapping(value = "/poll", method = RequestMethod.POST)
	public String poll() {
		poller.poll();
		return "redirect:/";
	}

}
