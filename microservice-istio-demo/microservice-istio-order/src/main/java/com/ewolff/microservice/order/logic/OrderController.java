package com.ewolff.microservice.order.logic;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@GetMapping("/form.html")
	public ModelAndView form() {
		ModelAndView modelAndView = new ModelAndView("orderForm", "order", new Order());
		modelAndView.addObject("items", itemRepository.findAll(Sort.unsorted()));
		modelAndView.addObject("customers", customerRepository.findAll(Sort.unsorted()));
		return modelAndView;
	}

	@PostMapping("/line")
	public ModelAndView addLine(Order order) {
		order.addLine(0, itemRepository.findAll(Sort.unsorted()).iterator().next());
		ModelAndView modelAndView = new ModelAndView("orderForm", "order", order);
		modelAndView.addObject("items", itemRepository.findAll(Sort.unsorted()));
		modelAndView.addObject("customers", customerRepository.findAll(Sort.unsorted()));
		return modelAndView;
	}

	@GetMapping("/{id}")
	public ModelAndView get(@PathVariable long id) {
		return new ModelAndView("order", "order", orderRepository.findById(id).get());
	}

	@GetMapping("/order/{id}")
	public ResponseEntity<Order> getJSON(@PathVariable long id) {
		Optional<Order> response = orderRepository.findById(id);
		if (response.isEmpty()) {
			return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Order>(response.get(), HttpStatus.OK);
		}
	}

	@GetMapping("/full-{id}")
	public ModelAndView full(@PathVariable long id) {
		return new ModelAndView("order-full", "order", orderRepository.findById(id).get());
	}

	@PostMapping("/")
	public ModelAndView post(Order order) {
		order = orderService.order(order);
		return new ModelAndView("success");
	}

	@DeleteMapping("/{id}")
	public ModelAndView delete(@PathVariable long id) {
		orderRepository.deleteById(id);

		return new ModelAndView("success");
	}

}
