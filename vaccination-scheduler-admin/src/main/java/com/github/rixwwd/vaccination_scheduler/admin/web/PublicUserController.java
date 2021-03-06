package com.github.rixwwd.vaccination_scheduler.admin.web;

import java.io.IOException;
import java.util.UUID;

import javax.validation.groups.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.admin.entity.PublicUser.Create;
import com.github.rixwwd.vaccination_scheduler.admin.entity.PublicUser.UpdatePassword;
import com.github.rixwwd.vaccination_scheduler.admin.exception.NotFoundException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.PublicUserRepository;
import com.github.rixwwd.vaccination_scheduler.admin.service.PublicUserUploadService;

@PreAuthorize("isAuthenticated() && hasAuthority('PUBLIC_USER')")
@Controller
public class PublicUserController {

	private final Logger logger = LoggerFactory.getLogger(PublicUserController.class);

	private final PublicUserRepository publicUserRepository;

	private final PublicUserUploadService publicUserUploadService;

	private final PasswordEncoder encoder;

	public PublicUserController(PublicUserRepository publicUserRepository,
			PublicUserUploadService publicUserUploadService, PasswordEncoder encoder) {
		this.publicUserRepository = publicUserRepository;
		this.publicUserUploadService = publicUserUploadService;
		this.encoder = encoder;
	}

	@GetMapping("/publicUser/")
	public ModelAndView index(Pageable pageable) {

		var modelAndView = new ModelAndView("publicUser/index");
		var publicUsers = publicUserRepository.findAll(pageable);
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

		publicUser.changePassword(encoder);
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
			publicUser.setId(id);
			modelAndView.addObject("publicUser", publicUser);
			return modelAndView;
		}

		var updatedPublicUser = publicUserRepository.findById(id).orElseThrow(NotFoundException::new);

		updatedPublicUser.setName(publicUser.getName());
		updatedPublicUser.setHurigana(publicUser.getHurigana());
		updatedPublicUser.setBirthday(publicUser.getBirthday());
		updatedPublicUser.setAddress(publicUser.getAddress());
		var passwordChanged = updatedPublicUser.changePassword(encoder);

		var newPublicUser = publicUserRepository.save(updatedPublicUser);

		if (passwordChanged) {
			logger.info("パスワードを変更しました。 PublicUser = " + updatedPublicUser);
		}

		return new ModelAndView("redirect:/publicUser/" + newPublicUser.getId());
	}

	@DeleteMapping("/publicUser/{id}")
	public String delete(@PathVariable UUID id) {

		var publicUser = publicUserRepository.findById(id).orElseThrow(NotFoundException::new);
		publicUserRepository.delete(publicUser);

		return "redirect:/publicUser/";
	}

	@GetMapping("/publicUser/upload")
	public ModelAndView upload() {
		return new ModelAndView("publicUser/upload");
	}

	@PostMapping("/publicUser/upload")
	public ModelAndView upload(@RequestParam("csv") MultipartFile file) throws IOException, BindException {

		publicUserUploadService.createPublicUserFromCsv(file.getInputStream());

		return new ModelAndView("redirect:/publicUser/");
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("loginName", "plainPassword", "plainPasswordConfirmation", "name", "hurigana",
				"birthday", "address", "telephoneNumber", "email", "sms");
	}
}
