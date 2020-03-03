package com.ewolff.microservice.invoicing.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ewolff.microservice.invoicing.poller.InvoicePoller;

@Controller
public class PollController {

	private InvoicePoller poller;

	@Autowired
	public PollController(InvoicePoller poller) {
		this.poller = poller;
	}

	@RequestMapping(value = "/poll", method = RequestMethod.POST)
	public String poll() {
		poller.poll();
		return "success";
	}

}
