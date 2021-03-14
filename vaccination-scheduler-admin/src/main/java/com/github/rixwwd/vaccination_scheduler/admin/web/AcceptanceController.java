package com.github.rixwwd.vaccination_scheduler.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.repository.ReservationRepository;

@Controller
public class AcceptanceController {

	private ReservationRepository reservationRepository;

	public AcceptanceController(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@GetMapping("/acceptance/")
	public ModelAndView index() {
		var modelAndView = new ModelAndView("acceptance/search");
		modelAndView.addObject("form", new AcceptanceForm());
		return modelAndView;

	}

	@PostMapping("/acceptance/")
	public ModelAndView search(@ModelAttribute("form") @Validated AcceptanceForm form, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("acceptance/search");
		}

		var reservation = reservationRepository.findByReservationNumber(form.getReservationNumber());

		var modelAndView = new ModelAndView("acceptance/result");
		modelAndView.addObject("reservation", reservation.orElse(null));
		return modelAndView;

	}

}
