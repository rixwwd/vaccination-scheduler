package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.repository.VaccinationHistoryRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.WaitingListRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.ReservationService;

@Controller
public class MenuController {

	private ReservationService reservationService;

	private VaccinationHistoryRepository vaccinationHistoryRepository;

	private WaitingListRepository waitingListRepository;

	public MenuController(ReservationService reservationService,
			VaccinationHistoryRepository vaccinationHistoryRepository, WaitingListRepository waitingListRepository) {
		this.reservationService = reservationService;
		this.vaccinationHistoryRepository = vaccinationHistoryRepository;
		this.waitingListRepository = waitingListRepository;
	}

	@GetMapping("/menu/")
	public ModelAndView index(@AuthenticationPrincipal PublicUser publicUser) {

		var reservation = reservationService.getReservation(publicUser.getId());
		var modelAndView = new ModelAndView("menu/index");
		modelAndView.addObject("reservation", reservation.orElse(null));

		var vaccinationHistories = vaccinationHistoryRepository
				.findByPublicUserIdOrderByVaccinatedAtAsc(publicUser.getId());
		modelAndView.addObject("vaccinationHistories", vaccinationHistories);

		var waitingList = waitingListRepository.findByPublicUserId(publicUser.getId());
		modelAndView.addObject("waitingList", waitingList);

		return modelAndView;
	}
}
