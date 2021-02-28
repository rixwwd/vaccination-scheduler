package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.service.ReservationService;

@Controller
public class MenuController {

	private ReservationService reservationService;

	public MenuController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping("/menu/")
	public ModelAndView index(@AuthenticationPrincipal PublicUser publicUser) {

		var reservation = reservationService.getReservation(publicUser.getId());
		var modelAndView = new ModelAndView("menu/index");
		modelAndView.addObject("reservation", reservation.isPresent() ? reservation.get() : null);
		return modelAndView;
	}
}
