package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Room;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.RoomRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.ReservationService;

@Controller
public class MenuController {

	private ReservationService reservationService;

	private CellRepository cellRepository;

	private RoomRepository roomRepository;

	public MenuController(ReservationService reservationService, CellRepository cellRepository,
			RoomRepository roomRepository) {
		this.reservationService = reservationService;
		this.cellRepository = cellRepository;
		this.roomRepository = roomRepository;
	}

	@GetMapping("/menu/")
	public ModelAndView index(@AuthenticationPrincipal PublicUser publicUser) {

		var reservation = reservationService.getReservation(publicUser.getId());

		Cell reservedCell = null;
		Room reservedRoom = null;
		if (reservation.isPresent()) {
			reservedCell = cellRepository.findById(reservation.get().getCellId()).get();
			reservedRoom = roomRepository.findById(reservedCell.getRoomId()).get();
		}

		var modelAndView = new ModelAndView("menu/index");
		modelAndView.addObject("reservation", reservation.isPresent() ? reservation.get() : null);
		modelAndView.addObject("reservedCell", reservedCell);
		modelAndView.addObject("reservedRoom", reservedRoom);
		return modelAndView;
	}
}
