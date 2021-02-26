package com.github.rixwwd.vaccination_scheduler.admin.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Vaccine;
import com.github.rixwwd.vaccination_scheduler.admin.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.VaccineRepository;

@Controller
public class VaccineController {

	private VaccineRepository vaccineRepository;

	public VaccineController(VaccineRepository vaccineRepository) {
		this.vaccineRepository = vaccineRepository;
	}

	@GetMapping("/vaccine/")
	public ModelAndView index() {

		var modelAndView = new ModelAndView("vaccine/index");
		var vaccines = vaccineRepository.findAll();
		modelAndView.addObject("vaccines", vaccines);
		return modelAndView;
	}

	@GetMapping("/vaccine/new")
	public ModelAndView newVaccin() {
		var modelAndView = new ModelAndView("vaccine/new");
		modelAndView.addObject("vaccine", new Vaccine());
		return modelAndView;
	}

	@PostMapping("/vaccine/")
	public ModelAndView create(@Validated Vaccine vaccine, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("vaccine/new");
			modelAndView.addObject("vaccine", vaccine);
			return modelAndView;
		}

		var newVaccine = vaccineRepository.save(vaccine);

		return new ModelAndView("redirect:/vaccine/" + newVaccine.getId());
	}

	@GetMapping("/vaccine/{id}")
	public ModelAndView show(@PathVariable UUID id) {
		var vaccine = vaccineRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("vaccine/show");
		modelAndView.addObject("vaccine", vaccine);
		return modelAndView;
	}

	@GetMapping("/vaccine/{id}/edit")
	public ModelAndView edit(@PathVariable UUID id) {
		var vaccine = vaccineRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("vaccine/edit");
		modelAndView.addObject("vaccine", vaccine);
		return modelAndView;

	}

	@PatchMapping("/vaccine/{id}")
	public ModelAndView update(@PathVariable UUID id, @Validated Vaccine vaccine, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("vaccine/edit");
			modelAndView.addObject("vaccine", vaccine);
			return modelAndView;
		}

		var updatedVaccine = vaccineRepository.findById(id).orElseThrow(NotFoundException::new);
		updatedVaccine.setExpectedDeliveryDate(vaccine.getExpectedDeliveryDate());
		updatedVaccine.setQuantity(vaccine.getQuantity());
		updatedVaccine.setRoomId(vaccine.getRoomId());

		var newVaccine = vaccineRepository.save(updatedVaccine);

		return new ModelAndView("redirect:/vaccine/" + newVaccine.getId());
	}

	@DeleteMapping("/vaccine/{id}")
	public String delete(@PathVariable UUID id) {

		var vaccine = vaccineRepository.findById(id).orElseThrow(NotFoundException::new);
		vaccineRepository.delete(vaccine);

		return "redirect:/vaccine/";
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("expectedDeliveryDate", "quantity", "roomId");
	}
}
