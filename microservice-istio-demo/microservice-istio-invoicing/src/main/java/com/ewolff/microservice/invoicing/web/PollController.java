package com.ewolff.microservice.invoicing.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.ewolff.microservice.invoicing.poller.InvoicePoller;

@Controller
public class PollController {

	private InvoicePoller poller;

	public PollController(InvoicePoller poller) {
		this.poller = poller;
	}

	@PostMapping("/poll")
	public String poll() {
		poller.poll();
		return "success";
	}

}
