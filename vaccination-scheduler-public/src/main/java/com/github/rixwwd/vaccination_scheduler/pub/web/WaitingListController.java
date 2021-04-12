package com.github.rixwwd.vaccination_scheduler.pub.web;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.WaitingListPk;
import com.github.rixwwd.vaccination_scheduler.pub.exception.DuplicateWaitingException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.WaitingListService;

@Controller
public class WaitingListController {

	private CellRepository cellRepository;

	private PublicUserRepository publicUserRepository;

	private WaitingListService waitingListService;

	public WaitingListController(CellRepository cellRepository, PublicUserRepository publicUserRepository,
			WaitingListService waitingListService) {
		this.cellRepository = cellRepository;
		this.publicUserRepository = publicUserRepository;
		this.waitingListService = waitingListService;
	}

	@GetMapping("/waitingList/new")
	public ModelAndView newWaitingList(PublicUser publicUser, @RequestParam("cellId") UUID cellId) {

		String errorMessage = null;
		if (publicUser.getEmail() == null || publicUser.getEmail().isEmpty()) {
			errorMessage = "連絡先のメールアドレスが未登録です。";
			return new ModelAndView("waitingList/new", Map.of("errorMessage", errorMessage));
		}

		var cell = cellRepository.findById(cellId);
		if (cell.isEmpty()) {
			errorMessage = "時間枠が存在しません。";
			return new ModelAndView("waitingList/new", Map.of("errorMessage", errorMessage));
		}

		return new ModelAndView("waitingList/new",
				Map.of("publicUser", publicUser, "cell", cell.orElseThrow(), "waitingListForm", new WaitingListForm()));
	}

	@PostMapping("/waitingList/")
	public ModelAndView create(PublicUser publicUser, @Validated WaitingListForm waitingListForm,
			BindingResult bindingResult) {

		String errorMessage = null;

		var cell = cellRepository.findById(waitingListForm.getCellId());
		if (cell.isEmpty()) {
			errorMessage = "時間枠が存在しません。";
			return new ModelAndView("waitingList/new", Map.of("errorMessage", errorMessage));
		}

		if (bindingResult.hasErrors()) {
			return new ModelAndView("waitingList/new", Map.of("publicUser", publicUser, "cell", cell.orElseThrow(),
					"waitingListForm", new WaitingListForm()));
		}

		try {
			waitingListService.addWaitingList(publicUser, cell.orElseThrow());
		} catch (DuplicateWaitingException e) {
			errorMessage = "すでにキャンセル待ちしています。";
			return new ModelAndView("waitingList/new", Map.of("errorMessage", errorMessage));
		}

		return new ModelAndView("redirect:/");
	}

	@DeleteMapping("/waitingList/{id}")
	public String delete(PublicUser publicUser, @PathVariable("id") UUID cellId) {

		waitingListService.delete(new WaitingListPk(cellId, publicUser.getId()));
		return "redirect:/menu/";
	}

	@ModelAttribute(binding = false)
	PublicUser publicUser(@AuthenticationPrincipal UserDetails userDetails) {

		return publicUserRepository.findByLoginName(userDetails.getUsername()).orElseThrow();
	}

}
