package com.github.rixwwd.vaccination_scheduler.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.entity.VaccinationHistory;
import com.github.rixwwd.vaccination_scheduler.admin.exception.NoAcceptanceException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.VaccineMismatchException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.ReservationRepository;
import com.github.rixwwd.vaccination_scheduler.admin.service.VaccinationService;

@Controller
public class VaccinationController {

	private ReservationRepository reservationRepository;

	private VaccinationService vaccinateionService;

	public VaccinationController(ReservationRepository reservationRepository, VaccinationService vaccinateionService) {
		this.reservationRepository = reservationRepository;
		this.vaccinateionService = vaccinateionService;
	}

	@GetMapping("/vaccination/")
	public ModelAndView index() {
		var modelAndView = new ModelAndView("vaccination/new");
		modelAndView.addObject("form", new VaccinationForm());
		return modelAndView;

	}

	@PostMapping("/vaccination/")
	public ModelAndView complete(@ModelAttribute("form") @Validated VaccinationForm form, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("vaccination/new");
		}

		var reservation = reservationRepository.findByReservationNumber(form.getReservationNumber());
		if (reservation.isEmpty()) {
			return new ModelAndView("vaccination/new");
		}

		VaccinationHistory vaccinationHistory = null;
		String errorMessage = null;
		try {
			vaccinationHistory = vaccinateionService.vaccinate(reservation.get(), form.getVaccine());
		} catch (NoAcceptanceException e) {
			errorMessage = "受付が済んでいません。";
		} catch (VaccineMismatchException e) {
			errorMessage = "前回接種したワクチンと異なるワクチンです。";
		}

		var modelAndView = new ModelAndView("vaccination/result");
		modelAndView.addObject("vaccinationHistory", vaccinationHistory);
		modelAndView.addObject("errorMessage", errorMessage);
		return modelAndView;
	}
}
