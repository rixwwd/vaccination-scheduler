package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.pub.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.pub.service.ReservationService;

@Controller
public class ReservationController {

	private ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {

		this.reservationService = reservationService;
	}

	@GetMapping("/reservation/new")
	public ModelAndView newReservation() {

		var modelAndView = new ModelAndView("reservation/new");
		modelAndView.addObject("reservation", new Reservation());
		return modelAndView;
	}

	@PostMapping("/reservation/confirm")
	public ModelAndView confirm(@Validated Reservation reservation, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("reservation/new");
		}

		var modelAndView = new ModelAndView("reservation/confirm");
		return modelAndView;
	}

	@PostMapping("/reservation/")
	public ModelAndView create(@AuthenticationPrincipal PublicUser publicUser, @Validated Reservation reservation,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("reservation/new");
		}

		reservation.setPublicUserId(publicUser.getId());
		reservationService.reserve(reservation, publicUser.getCoupon());

		return new ModelAndView("redirect:/reservation/");
	}

	@GetMapping("/reservation/")
	public ModelAndView complete(@AuthenticationPrincipal PublicUser publicUser) {

		var newReservation = reservationService.getReservation(publicUser.getId()).orElseThrow(NotFoundException::new);

		var modelAndView = new ModelAndView("reservation/complete");
		modelAndView.addObject("reservation", newReservation);
		return modelAndView;
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("cellId", "coupon");
	}
}
