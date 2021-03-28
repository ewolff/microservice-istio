package com.ewolff.microservice.invoicing;

import org.springframework.transaction.annotation.Transactional;

public interface InvoiceService {

	void generateInvoice(Invoice invoice);

}