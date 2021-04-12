package com.github.rixwwd.vaccination_scheduler.admin.web;

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

import com.github.rixwwd.vaccination_scheduler.admin.entity.AdminUser;
import com.github.rixwwd.vaccination_scheduler.admin.exception.InvalidPasswordException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.AdminUserRepository;
import com.github.rixwwd.vaccination_scheduler.admin.service.PasswordService;

@Controller
public class PasswordController {

	private final PasswordService passwordService;
	private final AdminUserRepository adminUserRepository;

	public PasswordController(PasswordService passwordService, AdminUserRepository adminUserRepository) {
		this.passwordService = passwordService;
		this.adminUserRepository = adminUserRepository;
	}

	@GetMapping("/password/edit")
	public ModelAndView edit() {
		var modelAndView = new ModelAndView("password/edit");
		modelAndView.addObject(new PasswordForm());
		return modelAndView;
	}

	@PatchMapping("/password/")
	public ModelAndView update(
			AdminUser adminUser, @Validated PasswordForm form, BindingResult bindingResult,
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
			passwordService.changePassword(adminUser, form.getPassword(), form.getNewPassword());
		} catch (InvalidPasswordException e) {
			bindingResult.rejectValue("password", "password.invalid");
			return new ModelAndView("password/edit");
		}
		redirectAttributes.addFlashAttribute("successMessage", "パスワードを変更しました。");

		return new ModelAndView("redirect:/menu/");
	}

	@ModelAttribute(binding = false)
	AdminUser adminUser(@AuthenticationPrincipal UserDetails userDetails) {
		return adminUserRepository.findByUsername(userDetails.getUsername()).orElseThrow();
	}
}
