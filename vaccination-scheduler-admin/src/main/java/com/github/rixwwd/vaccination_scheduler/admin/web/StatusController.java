package com.github.rixwwd.vaccination_scheduler.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.dto.VaccinationCount;
import com.github.rixwwd.vaccination_scheduler.admin.service.StatusService;

@Controller
public class StatusController {

	private StatusService statusService;

	public StatusController(StatusService statusService) {
		this.statusService = statusService;
	}

	@GetMapping("/status/dailyVaccinatedCounter")
	public ModelAndView dailyVaccinatedCounter() {

		var vaccinationCountList = statusService.countDailyVaccination();
		var totalCount = vaccinationCountList.stream().mapToLong(VaccinationCount::getCount).sum();

		var modelAndView = new ModelAndView("status/dailyVaccinatedCounter");
		modelAndView.addObject("vaccinationCountList", vaccinationCountList);
		modelAndView.addObject("totalCount", totalCount);
		return modelAndView;
	}
}
