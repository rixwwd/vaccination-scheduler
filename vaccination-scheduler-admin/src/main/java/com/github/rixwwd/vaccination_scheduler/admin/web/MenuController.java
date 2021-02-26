package com.github.rixwwd.vaccination_scheduler.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

	@GetMapping("/menu/")
	public String index() {
		return "menu/index";
	}
}
