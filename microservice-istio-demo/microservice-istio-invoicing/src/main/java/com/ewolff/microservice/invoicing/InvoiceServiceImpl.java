package com.ewolff.microservice.invoicing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

	private InvoiceRepository invoiceRepository;

	public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
		super();
		this.invoiceRepository = invoiceRepository;
	}

	@Override
	@Transactional
	public void generateInvoice(Invoice invoice) {
		if (invoiceRepository.existsById(invoice.getId())) {
			log.info("Invoice id {} already exists - ignored", invoice.getId());
		} else {
			invoiceRepository.save(invoice);
		}
	}

}
