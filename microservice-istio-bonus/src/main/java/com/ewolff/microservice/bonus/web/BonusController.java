package com.ewolff.microservice.bonus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ewolff.microservice.bonus.BonusRepository;

@Controller
public class BonusController {

	private BonusRepository bonusRepository;

	@Autowired
	public BonusController(BonusRepository bonusRepository) {
		this.bonusRepository = bonusRepository;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView bonus(@PathVariable("id") long id) {
		return new ModelAndView("bonus", "bonus", bonusRepository.findById(id).get());
	}

	@RequestMapping("/")
	public ModelAndView bonusList() {
		return new ModelAndView("bonuslist", "bonuses", bonusRepository.findAll());
	}

}
