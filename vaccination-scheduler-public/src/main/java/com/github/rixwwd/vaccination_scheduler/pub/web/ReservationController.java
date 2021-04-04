package com.github.rixwwd.vaccination_scheduler.pub.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.github.rixwwd.vaccination_scheduler.pub.entity.ActionType;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Room;
import com.github.rixwwd.vaccination_scheduler.pub.exception.DuplicateRservationException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.InvalidCouponException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.OverCapacityException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccinationTooEarlyException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccineMismatchException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.VaccineShortageException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.RoomRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.ActionLogService;
import com.github.rixwwd.vaccination_scheduler.pub.service.ReservationService;
import com.github.rixwwd.vaccination_scheduler.pub.service.VaccinationTimePastException;

@Controller
public class ReservationController {

	private final ReservationService reservationService;

	private final RoomRepository roomRepository;

	private final CellRepository cellRepository;

	private final PublicUserRepository publicUserRepository;

	private final ActionLogService actionLogService;

	public ReservationController(ReservationService reservationService, RoomRepository roomRepository,
			CellRepository cellRepository, PublicUserRepository publicUserRepository,
			ActionLogService actionLogService) {

		this.reservationService = reservationService;
		this.roomRepository = roomRepository;
		this.cellRepository = cellRepository;
		this.publicUserRepository = publicUserRepository;
		this.actionLogService = actionLogService;
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
	public ModelAndView create(PublicUser publicUser, @Validated Reservation reservation, BindingResult bindingResult) {

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

		actionLogService.log(publicUser, ActionType.RESERVE);

		return new ModelAndView("redirect:/reservation/");
	}

	@GetMapping("/reservation/")
	public ModelAndView complete(PublicUser publicUser) {

		return new ModelAndView("reservation/complete");
	}

	@DeleteMapping("/reservation/")
	public String delete(PublicUser publicUser) {

		reservationService.cancel(publicUser);
		actionLogService.log(publicUser, ActionType.RESERVE_CANCEL);
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
	List<Room> rooms(@AuthenticationPrincipal UserDetails user) {

		var publicUser = publicUserRepository.findByLoginName(user.getUsername()).orElseThrow();

		var histories = publicUser.getFirstVaccinationHistory();
		if (histories.isEmpty()) {
			return roomRepository.findAll();
		}
		return roomRepository.findByVaccine(histories.get().getVaccine());
	}

	/**
	 * １回目に接種したワクチンと同じワクチンを接種できる会場の時間枠を返す。初めてならすべての会場の時間枠を返す。
	 * 
	 * @param publicUser ログインユーザー
	 * @return {@link Cell}
	 */
	@ModelAttribute("cells")
	List<Cell> cells(@AuthenticationPrincipal UserDetails user) {

		var now = LocalDateTime.now();

		var publicUser = publicUserRepository.findByLoginName(user.getUsername()).orElseThrow();

		var firstTime = publicUser.getFirstVaccinationHistory();
		if (firstTime.isEmpty()) {
			return cellRepository.findByBeginTimeAfter(now);
		}
		var dosesDay = firstTime.get().getVaccinatedAt()
				.plusDays(firstTime.get().getVaccine().getDosesInterval().toDays());
		return cellRepository.findByVaccineAndBeginTimeAfter(firstTime.get().getVaccine(), dosesDay.atStartOfDay());
	}

	@ModelAttribute(binding = false)
	PublicUser publicUser(@AuthenticationPrincipal UserDetails user) {
		return publicUserRepository.findByLoginName(user.getUsername()).orElseThrow();
	}
}
