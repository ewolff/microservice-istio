package com.ewolff.microservice.bonus.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ewolff.microservice.bonus.BonusRepository;

@Controller
public class BonusController {

	private BonusRepository bonusRepository;

	public BonusController(BonusRepository bonusRepository) {
		this.bonusRepository = bonusRepository;
	}

	@GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView bonus(@PathVariable long id) {
		return new ModelAndView("bonus", "bonus", bonusRepository.findById(id).get());
	}

	@RequestMapping("/")
	public ModelAndView bonusList() {
		return new ModelAndView("bonuslist", "bonuses", bonusRepository.findAll());
	}

}
