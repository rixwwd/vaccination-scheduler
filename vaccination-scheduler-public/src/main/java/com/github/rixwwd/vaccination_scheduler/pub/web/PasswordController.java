package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.exception.InvalidPasswordException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.PasswordService;

@Controller
public class PasswordController {

	private final PasswordService passwordService;

	private final PublicUserRepository publicUserRepository;

	public PasswordController(PasswordService passwordService, PublicUserRepository publicUserRepository) {
		this.passwordService = passwordService;
		this.publicUserRepository = publicUserRepository;
	}

	@GetMapping("/password/edit")
	public ModelAndView edit() {
		var modelAndView = new ModelAndView("password/edit");
		modelAndView.addObject(new PasswordForm());
		return modelAndView;
	}

	@PatchMapping("/password/")
	public ModelAndView update(PublicUser publicUser, @Validated PasswordForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("password/edit");
		}

		if (!form.isMatchPassword()) {
			bindingResult.rejectValue("newPassword", "password.mismatch");
			bindingResult.rejectValue("newPasswordConfirmation", "password.mismatch");
			return new ModelAndView("password/edit");
		}

		try {
			passwordService.changePassword(publicUser, form.getPassword(), form.getNewPassword());
		} catch (InvalidPasswordException e) {
			bindingResult.rejectValue("password", "password.invalid");
			return new ModelAndView("password/edit");
		}
		redirectAttributes.addFlashAttribute("successMessage", "パスワードを変更しました。");

		return new ModelAndView("redirect:/menu/");
	}

	@ModelAttribute(binding = false)
	PublicUser publicUser(@AuthenticationPrincipal UserDetails userDetails) {

		return publicUserRepository.findByLoginName(userDetails.getUsername()).orElseThrow();
	}
}
