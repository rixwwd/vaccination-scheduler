package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SignInController {

	@GetMapping("/signIn")
	public ModelAndView signIn() {
		return new ModelAndView("signIn/signIn");
	}
}
