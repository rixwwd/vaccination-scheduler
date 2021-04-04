package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;

@Controller
public class MenuController {

	private final PublicUserRepository publicUserRepository;

	public MenuController(PublicUserRepository publicUserRepository) {
		this.publicUserRepository = publicUserRepository;
	}

	@GetMapping("/menu/")
	public ModelAndView index() {

		return new ModelAndView("menu/index");
	}

	@ModelAttribute(binding = false)
	public PublicUser publicUser(@AuthenticationPrincipal UserDetails user) {
		return publicUserRepository.findByLoginName(user.getUsername()).orElseThrow();
	}
}
