package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;

@Controller
public class ContactController {

	private PublicUserRepository publicUserRepository;

	public ContactController(PublicUserRepository publicUserRepository) {
		this.publicUserRepository = publicUserRepository;
	}

	@GetMapping("/contact/edit")
	public ModelAndView edit(@AuthenticationPrincipal PublicUser publicUser) {

		var pu = publicUserRepository.findById(publicUser.getId()).orElseThrow();
		var form = new ContactForm(pu.getTelephoneNumber(), pu.getEmail(), pu.getSms());

		var modelAndView = new ModelAndView("contact/edit");
		modelAndView.addObject("form", form);
		return modelAndView;
	}

	@PatchMapping("/contact/")
	public ModelAndView update(@AuthenticationPrincipal PublicUser publicUser, @Validated ContactForm form,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			var edit = new ModelAndView("contact/edit");
			edit.addObject("form", form);
			return edit;
		}

		var newPublicUser = publicUserRepository.findById(publicUser.getId()).orElseThrow();
		newPublicUser.setTelephoneNumber(form.getTelephoneNumber());
		newPublicUser.setEmail(form.getEmail());
		newPublicUser.setSms(form.getSms());

		publicUserRepository.save(newPublicUser);

		redirectAttributes.addFlashAttribute("successMessage", "連絡先を更新しました。");

		return new ModelAndView("redirect:/contact/edit");
	}
}
