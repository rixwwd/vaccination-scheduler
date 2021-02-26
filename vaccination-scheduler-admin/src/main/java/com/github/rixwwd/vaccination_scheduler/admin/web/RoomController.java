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

import com.github.rixwwd.vaccination_scheduler.admin.entity.Room;
import com.github.rixwwd.vaccination_scheduler.admin.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.RoomRepository;

@Controller
public class RoomController {

	private RoomRepository roomRepository;

	public RoomController(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	@GetMapping("/room/")
	public ModelAndView index() {

		var modelAndView = new ModelAndView("room/index");
		var rooms = roomRepository.findAll();
		modelAndView.addObject("rooms", rooms);
		return modelAndView;
	}

	@GetMapping("/room/new")
	public ModelAndView newRoom() {
		var modelAndView = new ModelAndView("room/new");
		modelAndView.addObject("room", new Room());
		return modelAndView;
	}

	@PostMapping("/room/")
	public ModelAndView create(@Validated Room room, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("room/new");
			modelAndView.addObject("room", room);
			return modelAndView;
		}

		var newRoom = roomRepository.save(room);

		return new ModelAndView("redirect:/room/" + newRoom.getId());
	}

	@GetMapping("/room/{id}")
	public ModelAndView show(@PathVariable UUID id) {
		var room = roomRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("room/show");
		modelAndView.addObject("room", room);
		return modelAndView;
	}

	@GetMapping("/room/{id}/edit")
	public ModelAndView edit(@PathVariable UUID id) {
		var room = roomRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("room/edit");
		modelAndView.addObject("room", room);
		return modelAndView;

	}

	@PatchMapping("/room/{id}")
	public ModelAndView update(@PathVariable UUID id, @Validated Room room, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("room/edit");
			modelAndView.addObject("room", room);
			return modelAndView;
		}

		var updatedRoom = roomRepository.findById(id).orElseThrow(NotFoundException::new);
		updatedRoom.setName(room.getName());

		var newRoom = roomRepository.save(updatedRoom);

		return new ModelAndView("redirect:/room/" + newRoom.getId());
	}

	@DeleteMapping("/room/{id}")
	public String delete(@PathVariable UUID id) {

		var room = roomRepository.findById(id).orElseThrow(NotFoundException::new);
		roomRepository.delete(room);

		return "redirect:/room/";
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("name");
	}

}
