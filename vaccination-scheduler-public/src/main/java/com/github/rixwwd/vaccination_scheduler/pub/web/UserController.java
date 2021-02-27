package com.github.rixwwd.vaccination_scheduler.pub.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

	@GetMapping("/user/edit")
	public ModelAndView edit() {
		return new ModelAndView("user/edit");
	}

	@PatchMapping("/user/")
	public ModelAndView update() {
		return new ModelAndView("redirect:/user/edit");
	}

}
