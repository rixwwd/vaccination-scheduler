package com.github.rixwwd.vaccination_scheduler.pub.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.github.rixwwd.vaccination_scheduler.pub.exception.DuplicateRservationException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.InvalidCouponException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.OverCapacityException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccinationTooEarlyException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccineMismatchException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccineShortageException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.RoomRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.VaccinationHistoryRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.ReservationService;
import com.github.rixwwd.vaccination_scheduler.pub.service.VaccinationTimePastException;

@Controller
public class ReservationController {

	private ReservationService reservationService;

	private RoomRepository roomRepository;

	private CellRepository cellRepository;

	private VaccinationHistoryRepository vaccinationHistoryRepository;

	public ReservationController(ReservationService reservationService, RoomRepository roomRepository,
			CellRepository cellRepository, VaccinationHistoryRepository vaccinationHistoryRepository) {

		this.reservationService = reservationService;
		this.roomRepository = roomRepository;
		this.cellRepository = cellRepository;
		this.vaccinationHistoryRepository = vaccinationHistoryRepository;
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

		String errorMessage = null;
		try {
			reservationService.reserve(reservation, publicUser);
		} catch (InvalidCouponException e) {
			errorMessage = "クーポンが不正です";
		} catch (VaccineShortageException e) {
			errorMessage = "ワクチンが不足しています";
		} catch (OverCapacityException e) {
			errorMessage = "定員を超えています";
		} catch (DuplicateRservationException e) {
			errorMessage = "予約が重複しています";
		} catch (VaccinationTimePastException e) {
			errorMessage = "予約枠は過去です";
		} catch (VaccineMismatchException e) {
			errorMessage = "前回接種したワクチンと異なるワクチンで予約しようとしています";
		} catch (VaccinationTooEarlyException e) {
			errorMessage = "２回目の接種は早すぎます。";
		}
		if (errorMessage != null) {
			return new ModelAndView("reservation/new", Map.of("errorMessage", errorMessage));
		}

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

	/**
	 * １回目に接種したワクチンと同じワクチンを接種できる会場を返す。初めてならすべての会場を返す。
	 * 
	 * @param publicUser ログインユーザー
	 * @return {@link Room}
	 */
	@ModelAttribute("rooms")
	List<Room> rooms(@AuthenticationPrincipal PublicUser publicUser) {

		var histories = vaccinationHistoryRepository.findByPublicUserIdOrderByVaccinatedAtAsc(publicUser.getId());
		if (histories.isEmpty()) {
			return roomRepository.findAll();
		}
		return roomRepository.findByVaccine(histories.get(0).getVaccine());
	}

	/**
	 * １回目に接種したワクチンと同じワクチンを接種できる会場の時間枠を返す。初めてならすべての会場の時間枠を返す。
	 * 
	 * @param publicUser ログインユーザー
	 * @return {@link Cell}
	 */
	@ModelAttribute("cells")
	List<Cell> cells(@AuthenticationPrincipal PublicUser publicUser) {

		var now = LocalDateTime.now();

		var histories = vaccinationHistoryRepository.findByPublicUserIdOrderByVaccinatedAtAsc(publicUser.getId());
		if (histories.isEmpty()) {
			return cellRepository.findByBeginTimeAfter(now);
		}
		var firstTime = histories.get(0);
		var dosesDay = firstTime.getVaccinatedAt().plus(firstTime.getVaccine().getDosesInterval());
		return cellRepository
				.findByRoomIdAndVaccineAndBeginTimeAfter(firstTime.getRoomId(), firstTime.getVaccine(), now).stream()
				.filter(c -> !c.getBeginTime().toLocalDate().isBefore(dosesDay))
				.collect(Collectors.toUnmodifiableList());
	}
}
