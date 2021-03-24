package com.github.rixwwd.vaccination_scheduler.admin.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rixwwd.vaccination_scheduler.admin.entity.AdminUser;
import com.github.rixwwd.vaccination_scheduler.admin.exception.InvalidPasswordException;
import com.github.rixwwd.vaccination_scheduler.admin.service.PasswordService;

@Controller
public class PasswordController {

	private PasswordService passwordService;

	public PasswordController(PasswordService passwordService) {
		this.passwordService = passwordService;
	}

	@GetMapping("/password/edit")
	public ModelAndView edit() {
		var modelAndView = new ModelAndView("password/edit");
		modelAndView.addObject(new PasswordForm());
		return modelAndView;
	}

	@PatchMapping("/password/")
	public ModelAndView update(@AuthenticationPrincipal AdminUser adminUser, @Validated PasswordForm form,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("password/edit");
		}

		if (!form.isMatchPassword()) {
			bindingResult.rejectValue("newPassword", "password.mismatch");
			bindingResult.rejectValue("newPasswordConfirmation", "password.mismatch");
			return new ModelAndView("password/edit");
		}

		try {
			passwordService.changePassword(adminUser, form.getPassword(), form.getNewPassword());
		} catch (InvalidPasswordException e) {
			bindingResult.rejectValue("password", "password.invalid");
			return new ModelAndView("password/edit");
		}
		redirectAttributes.addFlashAttribute("successMessage", "パスワードを変更しました。");

		return new ModelAndView("redirect:/menu/");
	}
}
