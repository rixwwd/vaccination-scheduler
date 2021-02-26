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

import com.github.rixwwd.vaccination_scheduler.admin.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.admin.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.CellRepository;

@Controller
public class CellController {

	private CellRepository cellRepository;

	public CellController(CellRepository cellRepository) {
		this.cellRepository = cellRepository;
	}

	@GetMapping("/cell/")
	public ModelAndView index() {

		var modelAndView = new ModelAndView("cell/index");
		var cells = cellRepository.findAll();
		modelAndView.addObject("cells", cells);
		return modelAndView;
	}

	@GetMapping("/cell/new")
	public ModelAndView newCell() {
		var modelAndView = new ModelAndView("cell/new");
		modelAndView.addObject("cell", new Cell());
		return modelAndView;
	}

	@PostMapping("/cell/")
	public ModelAndView create(@Validated Cell cell, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("cell/new");
			modelAndView.addObject("cell", cell);
			return modelAndView;
		}

		var newCell = cellRepository.save(cell);

		return new ModelAndView("redirect:/cell/" + newCell.getId());
	}

	@GetMapping("/cell/{id}")
	public ModelAndView show(@PathVariable UUID id) {
		var cell = cellRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("cell/show");
		modelAndView.addObject("cell", cell);
		return modelAndView;
	}

	@GetMapping("/cell/{id}/edit")
	public ModelAndView edit(@PathVariable UUID id) {
		var cell = cellRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("cell/edit");
		modelAndView.addObject("cell", cell);
		return modelAndView;

	}

	@PatchMapping("/cell/{id}")
	public ModelAndView update(@PathVariable UUID id, @Validated Cell cell, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("cell/edit");
			modelAndView.addObject("cell", cell);
			return modelAndView;
		}

		var updatedCell = cellRepository.findById(id).orElseThrow(NotFoundException::new);
		updatedCell.setRoomId(cell.getRoomId());
		updatedCell.setBeginTime(cell.getBeginTime());
		updatedCell.setEndTime(cell.getEndTime());
		updatedCell.setMaxNumberOfPeople(cell.getMaxNumberOfPeople());

		var newCell = cellRepository.save(updatedCell);

		return new ModelAndView("redirect:/cell/" + newCell.getId());
	}

	@DeleteMapping("/cell/{id}")
	public String delete(@PathVariable UUID id) {

		var cell = cellRepository.findById(id).orElseThrow(NotFoundException::new);
		cellRepository.delete(cell);

		return "redirect:/cell/";
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("roomId", "beginTime", "endTime", "maxNumberOfPeople");
	}
}
