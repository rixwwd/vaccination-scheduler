package com.github.rixwwd.vaccination_scheduler.pub.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Room;
import com.github.rixwwd.vaccination_scheduler.pub.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.RoomRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.ReservationService;

@Controller
public class ReservationController {

	private ReservationService reservationService;

	private RoomRepository roomRepository;

	private CellRepository cellRepository;

	public ReservationController(ReservationService reservationService, RoomRepository roomRepository,
			CellRepository cellRepository) {

		this.reservationService = reservationService;
		this.roomRepository = roomRepository;
		this.cellRepository = cellRepository;
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

		var cell = cellRepository.findById(reservation.getCellId()).orElseThrow();
		var modelAndView = new ModelAndView("reservation/confirm");
		modelAndView.addObject("cell", cell);
		return modelAndView;
	}

	@PostMapping("/reservation/")
	public ModelAndView create(@AuthenticationPrincipal PublicUser publicUser, @Validated Reservation reservation,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("reservation/new");
		}

		reservation.setPublicUserId(publicUser.getId());
		reservationService.reserve(reservation, publicUser);

		return new ModelAndView("redirect:/reservation/");
	}

	@GetMapping("/reservation/")
	public ModelAndView complete(@AuthenticationPrincipal PublicUser publicUser) {

		var newReservation = reservationService.getReservation(publicUser.getId()).orElseThrow(NotFoundException::new);

		var modelAndView = new ModelAndView("reservation/complete");
		modelAndView.addObject("reservation", newReservation);
		return modelAndView;
	}

	@DeleteMapping("/reservation/")
	public String delete(@AuthenticationPrincipal PublicUser publicUser) {

		var reservation = reservationService.getReservation(publicUser.getId()).orElseThrow(NotFoundException::new);
		reservationService.cancel(reservation);
		return "redirect:/menu/";
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("cellId", "coupon");
	}

	@ModelAttribute("rooms")
	List<Room> rooms() {
		return roomRepository.findAll();
	}

	@ModelAttribute("cells")
	List<Cell> cells() {
		return cellRepository.findAll();
	}
}
