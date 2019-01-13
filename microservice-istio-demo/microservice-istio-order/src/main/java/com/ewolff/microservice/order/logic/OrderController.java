package com.ewolff.microservice.order.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.ewolff.microservice.order.customer.Customer;
import com.ewolff.microservice.order.customer.CustomerRepository;
import com.ewolff.microservice.order.item.ItemRepository;

@Controller
class OrderController {

	private final Logger log = LoggerFactory.getLogger(OrderController.class);

	private OrderRepository orderRepository;

	private OrderService orderService;

	private CustomerRepository customerRepository;
	private ItemRepository itemRepository;

	@Autowired
	private OrderController(OrderService orderService, OrderRepository orderRepository,
			CustomerRepository customerRepository, ItemRepository itemRepository) {
		super();
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.itemRepository = itemRepository;
		this.orderService = orderService;
	}

	@ModelAttribute("items")
	public Iterable<com.ewolff.microservice.order.item.Item> items() {
		return itemRepository.findAll();
	}

	@ModelAttribute("customers")
	public Iterable<Customer> customers() {
		return customerRepository.findAll();
	}

	@RequestMapping("/")
	public ModelAndView orderList() {
		return new ModelAndView("orderlist", "orders", orderRepository.findAll());
	}

	@RequestMapping(value = "/feed", produces = "application/atom+xml")
	public ModelAndView orderFeed(WebRequest webRequest) {
		if ((orderRepository.lastUpdate() != null)
				&& (webRequest.checkNotModified(orderRepository.lastUpdate().getTime()))) {
			log.trace("Not Modified returned - request with If-Modified-Since {}",
					webRequest.getHeader(HttpHeaders.IF_MODIFIED_SINCE));
			return null;
		}
		log.trace("Returned Feed");
		return new ModelAndView(new OrderAtomFeedView(orderRepository), "orders", orderRepository.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView form() {
		return new ModelAndView("orderForm", "order", new Order());
	}

	@RequestMapping(value = "/line", method = RequestMethod.POST)
	public ModelAndView addLine(Order order) {
		order.addLine(0, itemRepository.findAll().iterator().next());
		return new ModelAndView("orderForm", "order", order);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable("id") long id) {
		return new ModelAndView("order", "order", orderRepository.findById(id).get());
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView post(Order order) {
		order = orderService.order(order);
		return new ModelAndView("success");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView post(@PathVariable("id") long id) {
		orderRepository.deleteById(id);

		return new ModelAndView("success");
	}

}
