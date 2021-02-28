package com.github.rixwwd.vaccination_scheduler.admin.web;

import java.util.UUID;

import javax.validation.groups.Default;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.github.rixwwd.vaccination_scheduler.admin.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.admin.entity.PublicUser.Create;
import com.github.rixwwd.vaccination_scheduler.admin.entity.PublicUser.UpdatePassword;
import com.github.rixwwd.vaccination_scheduler.admin.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.PublicUserRepository;

@Controller
public class PublicUserController {

	private PublicUserRepository publicUserRepository;

	public PublicUserController(PublicUserRepository publicUserRepository) {
		this.publicUserRepository = publicUserRepository;
	}

	@GetMapping("/publicUser/")
	public ModelAndView index() {

		var modelAndView = new ModelAndView("publicUser/index");
		var publicUsers = publicUserRepository.findAll();
		modelAndView.addObject("publicUsers", publicUsers);
		return modelAndView;
	}

	@GetMapping("/publicUser/new")
	public ModelAndView newPublicUser() {
		var modelAndView = new ModelAndView("publicUser/new");
		modelAndView.addObject("publicUser", new PublicUser());
		return modelAndView;
	}

	@PostMapping("/publicUser/")
	public ModelAndView create(@Validated(value = { Default.class, Create.class }) PublicUser publicUser,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("publicUser/new");
			modelAndView.addObject("publicUser", publicUser);
			return modelAndView;
		}

		// FIXME plainPasswordとplainPasswordCtonfirmationの一致確認
		var passwordEncoder = new BCryptPasswordEncoder();
		publicUser.setPassword("{bcrypt}" + passwordEncoder.encode(publicUser.getPlainPassword()));

		var newPublicUser = publicUserRepository.save(publicUser);

		return new ModelAndView("redirect:/publicUser/" + newPublicUser.getId());
	}

	@GetMapping("/publicUser/{id}")
	public ModelAndView show(@PathVariable UUID id) {
		var publicUser = publicUserRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("publicUser/show");
		modelAndView.addObject("publicUser", publicUser);
		return modelAndView;
	}

	@GetMapping("/publicUser/{id}/edit")
	public ModelAndView edit(@PathVariable UUID id) {
		var publicUser = publicUserRepository.findById(id).orElseThrow(NotFoundException::new);
		var modelAndView = new ModelAndView("publicUser/edit");
		modelAndView.addObject("publicUser", publicUser);
		return modelAndView;

	}

	@PatchMapping("/publicUser/{id}")
	public ModelAndView update(@PathVariable UUID id,
			@Validated(value = { Default.class, UpdatePassword.class }) PublicUser publicUser,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			var modelAndView = new ModelAndView("publicUser/edit");
			modelAndView.addObject("publicUser", publicUser);
			return modelAndView;
		}

		var updatedPublicUser = publicUserRepository.findById(id).orElseThrow(NotFoundException::new);

		// FIXME plainPasswordとplainPasswordCtonfirmationの一致確認
		if (publicUser.getPlainPassword() != null && publicUser.getPlainPassword().isEmpty()) {
			var passwordEncoder = new BCryptPasswordEncoder();
			updatedPublicUser.setPassword("{bcrypt}" + passwordEncoder.encode(publicUser.getPlainPassword()));
		}

		updatedPublicUser.setCoupon(publicUser.getCoupon());
		updatedPublicUser.setName(publicUser.getName());
		updatedPublicUser.setHurigana(publicUser.getHurigana());
		updatedPublicUser.setBirthday(publicUser.getBirthday());
		updatedPublicUser.setAddress(publicUser.getAddress());

		var newPublicUser = publicUserRepository.save(updatedPublicUser);

		return new ModelAndView("redirect:/publicUser/" + newPublicUser.getId());
	}

	@DeleteMapping("/publicUser/{id}")
	public String delete(@PathVariable UUID id) {

		var publicUser = publicUserRepository.findById(id).orElseThrow(NotFoundException::new);
		publicUserRepository.delete(publicUser);

		return "redirect:/publicUser/";
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("loginName", "plainPassword", "plainPasswordConfirmation", "coupon", "name", "hurigana",
				"birthday", "address");
	}
}
