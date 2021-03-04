package com.github.rixwwd.vaccination_scheduler.admin.web;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Room;
import com.github.rixwwd.vaccination_scheduler.admin.entity.VaccineStock;
import com.github.rixwwd.vaccination_scheduler.admin.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.RoomRepository;
import com.github.rixwwd.vaccination_scheduler.admin.repository.VaccineStockRepository;

@Controller
public class VaccineStockController {

	private VaccineStockRepository vaccineStockRepository;

	private RoomRepository roomRepository;

	public VaccineStockController(RoomRepository roomRepository, VaccineStockRepository vaccineStockRepository) {
		this.vaccineStockRepository = vaccineStockRepository;
		this.roomRepository = roomRepository;
	}

	@GetMapping("/vaccineStock/")
	public ModelAndView index() {

		var modelAndView = new ModelAndView("vaccineStock/index");
		var vaccineStocks = vaccineStockRepository.findAll();
		modelAndView.addObject("vaccineStocks", vaccineStocks);
		return modelAndView;
	}

	@GetMapping("/vaccineStock/new")
	public ModelAndView newVaccin() {
		var modelAndView = new ModelAndView("vaccineStock/new");
		modelAndView.addObject("vaccineStock", new VaccineStock());
		return modelAndView;
	}

	@PostMapping("/vaccineStock/")
	public ModelAndView create(@Validated VaccineStock vaccineStock, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("vaccineStock/new");
			modelAndView.addObject("vaccineStock", vaccineStock);
			return modelAndView;
		}

		var newVaccineStock = vaccineStockRepository.save(vaccineStock);

		return new ModelAndView("redirect:/vaccineStock/" + newVaccineStock.getId());
	}

	@GetMapping("/vaccineStock/{id}")
	public ModelAndView show(@PathVariable UUID id) {
		var vaccineStock = vaccineStockRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("vaccineStock/show");
		modelAndView.addObject("vaccineStock", vaccineStock);
		return modelAndView;
	}

	@GetMapping("/vaccineStock/{id}/edit")
	public ModelAndView edit(@PathVariable UUID id) {
		var vaccineStock = vaccineStockRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("vaccineStock/edit");
		modelAndView.addObject("vaccineStock", vaccineStock);
		return modelAndView;

	}

	@PatchMapping("/vaccineStock/{id}")
	public ModelAndView update(@PathVariable UUID id, @Validated VaccineStock vaccineStock,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("vaccineStock/edit");
			modelAndView.addObject("vaccineStock", vaccineStock);
			return modelAndView;
		}

		var updatedVaccineStock = vaccineStockRepository.findById(id).orElseThrow(NotFoundException::new);
		updatedVaccineStock.setExpectedDeliveryDate(vaccineStock.getExpectedDeliveryDate());
		updatedVaccineStock.setQuantity(vaccineStock.getQuantity());
		updatedVaccineStock.setRoomId(vaccineStock.getRoomId());

		var newVaccineStock = vaccineStockRepository.save(updatedVaccineStock);

		return new ModelAndView("redirect:/vaccineStock/" + newVaccineStock.getId());
	}

	@DeleteMapping("/vaccineStock/{id}")
	public String delete(@PathVariable UUID id) {

		var vaccineStock = vaccineStockRepository.findById(id).orElseThrow(NotFoundException::new);
		vaccineStockRepository.delete(vaccineStock);

		return "redirect:/vaccineStock/";
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("expectedDeliveryDate", "quantity", "roomId");
	}

	@ModelAttribute("rooms")
	List<Room> rooms() {
		return roomRepository.findAll();
	}
}
