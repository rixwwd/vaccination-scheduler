package com.github.rixwwd.vaccination_scheduler.admin.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.admin.exception.AcceptanceNotStartedException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.DoubleAcceptanceException;
import com.github.rixwwd.vaccination_scheduler.admin.exception.ReservationNotFoundException;
import com.github.rixwwd.vaccination_scheduler.admin.service.AcceptanceService;

@PreAuthorize("isAuthenticated() && hasAuthority('ACCEPTANCE')")
@Controller
public class AcceptanceController {

	private AcceptanceService acceptanceService;

	public AcceptanceController(AcceptanceService acceptanceService) {
		this.acceptanceService = acceptanceService;
	}

	@GetMapping("/acceptance/")
	public ModelAndView index() {
		var modelAndView = new ModelAndView("acceptance/search");
		modelAndView.addObject("form", new AcceptanceForm());
		return modelAndView;

	}

	@PostMapping("/acceptance/accept")
	public ModelAndView search(@ModelAttribute("form") @Validated AcceptanceForm form, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("acceptance/search");
		}

		Reservation reservation = null;
		String errorMessage = null;
		String successMessage = null;
		try {
			reservation = acceptanceService.accept(form.getReservationNumber());
			successMessage = "受け付けました。";
		} catch (ReservationNotFoundException e) {
			errorMessage = "予約は存在しません。";
		} catch (DoubleAcceptanceException e) {
			errorMessage = "受付済みです。";
			reservation = e.getReservation();
		} catch (AcceptanceNotStartedException e) {
			errorMessage = "受付は開始していません。";
			reservation = e.getReservation();
		}

		var modelAndView = new ModelAndView("acceptance/result");
		modelAndView.addObject("reservation", reservation);
		modelAndView.addObject("errorMessage", errorMessage);
		modelAndView.addObject("successMessage", successMessage);
		return modelAndView;

	}

}
