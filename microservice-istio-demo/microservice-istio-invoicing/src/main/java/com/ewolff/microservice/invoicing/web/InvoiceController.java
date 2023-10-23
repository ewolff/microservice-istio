package com.ewolff.microservice.invoicing.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ewolff.microservice.invoicing.InvoiceRepository;

@Controller
public class InvoiceController {

	private InvoiceRepository invoiceRepository;

	public InvoiceController(InvoiceRepository invoiceRepository) {
		this.invoiceRepository = invoiceRepository;
	}

	@GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView invoice(@PathVariable long id) {
		return new ModelAndView("invoice", "invoice", invoiceRepository.findById(id).get());
	}

	@RequestMapping("/")
	public ModelAndView invoiceList() {
		return new ModelAndView("invoicelist", "invoices", invoiceRepository.findAll());
	}

}
