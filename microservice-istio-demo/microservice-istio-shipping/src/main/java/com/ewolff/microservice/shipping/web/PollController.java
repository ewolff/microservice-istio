package com.ewolff.microservice.shipping.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ewolff.microservice.shipping.poller.ShippingPoller;

@Controller
public class PollController {

	private ShippingPoller poller;

	@Autowired
	public PollController(ShippingPoller poller) {
		this.poller = poller;
	}

	@RequestMapping(value = "/poll", method = RequestMethod.POST)
	public String poll() {
		poller.poll();
		return "success";
	}

}
