package com.ewolff.microservice.shipping.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.ewolff.microservice.shipping.poller.ShippingPoller;

@Controller
public class PollController {

	private ShippingPoller poller;

	public PollController(ShippingPoller poller) {
		this.poller = poller;
	}

	@PostMapping("/poll")
	public String poll() {
		poller.poll();
		return "success";
	}

}
