package com.ewolff.microservice.order.logic;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ewolff.microservice.order.customer.CustomerRepository;
import com.ewolff.microservice.order.item.ItemRepository;

@Controller
class OrderController {

	private OrderRepository orderRepository;

	private OrderService orderService;

	private CustomerRepository customerRepository;
	private ItemRepository itemRepository;

	public OrderController(OrderService orderService, OrderRepository orderRepository,
			CustomerRepository customerRepository, ItemRepository itemRepository) {
		super();
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.itemRepository = itemRepository;
		this.orderService = orderService;
	}

	@RequestMapping("/")
	public ModelAndView orderList() {
		return new ModelAndView("orderlist", "orders", orderRepository.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView form() {
		ModelAndView modelAndView = new ModelAndView("orderForm", "order", new Order());
		modelAndView.addObject("items", itemRepository.findAll(Sort.unsorted()));
		modelAndView.addObject("customers", customerRepository.findAll(Sort.unsorted()));
		return modelAndView;
	}

	@RequestMapping(value = "/line", method = RequestMethod.POST)
	public ModelAndView addLine(Order order) {
		order.addLine(0, itemRepository.findAll(Sort.unsorted()).iterator().next());
		ModelAndView modelAndView = new ModelAndView("orderForm", "order", order);
		modelAndView.addObject("items", itemRepository.findAll(Sort.unsorted()));
		modelAndView.addObject("customers", customerRepository.findAll(Sort.unsorted()));
		return modelAndView;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable("id") long id) {
		return new ModelAndView("order", "order", orderRepository.findById(id).get());
	}

	@RequestMapping(value = "/order/{id}", method = RequestMethod.GET)
	public ResponseEntity<Order> getJSON(@PathVariable("id") long id) {
		Optional<Order> response = orderRepository.findById(id);
		if (response.isEmpty()) {
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Order>(response.get(), HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/full-{id}", method = RequestMethod.GET)
	public ModelAndView full(@PathVariable("id") long id) {
		return new ModelAndView("order-full", "order", orderRepository.findById(id).get());
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView post(Order order) {
		order = orderService.order(order);
		return new ModelAndView("success");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") long id) {
		orderRepository.deleteById(id);

		return new ModelAndView("success");
	}

}
