package com.github.rixwwd.vaccination_scheduler.admin.web;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.groups.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import com.github.rixwwd.vaccination_scheduler.admin.entity.AdminUser;
import com.github.rixwwd.vaccination_scheduler.admin.entity.AdminUser.Create;
import com.github.rixwwd.vaccination_scheduler.admin.entity.AdminUser.Update;
import com.github.rixwwd.vaccination_scheduler.admin.repository.AdminUserRepository;

@Controller
@PreAuthorize("isAuthenticated() && hasAuthority('ADMIN_USER')")
public class AdminUserController {

	private final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

	private final AdminUserRepository adminUserRepository;

	private final SessionRegistry sessionRegistry;

	private final PasswordEncoder passwordEncoder;

	public AdminUserController(AdminUserRepository adminUserRepository, SessionRegistry sessionRegistry,
			PasswordEncoder passwordEncoder) {
		this.adminUserRepository = adminUserRepository;
		this.sessionRegistry = sessionRegistry;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/adminUser/")
	public ModelAndView index(Pageable pageable) {

		var adminUsers = adminUserRepository.findAll(pageable);
		return new ModelAndView("adminUser/index", Map.of("adminUsers", adminUsers));
	}

	@GetMapping("/adminUser/new")
	public ModelAndView newAdminUser() {
		return new ModelAndView("adminUser/new", Map.of("adminUser", new AdminUser()));
	}

	@PostMapping("/adminUser/")
	public ModelAndView create(@Validated({ Default.class, Create.class }) AdminUser adminUser,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ModelAndView("adminUser/new", Map.of("adminUser", adminUser));
		}

		adminUser.setEnabled(true);
		adminUser.changePassword(passwordEncoder);

		var newAdminUser = adminUserRepository.save(adminUser);

		logger.info("ユーザーを作成しました。 AdminUser=" + newAdminUser);

		return new ModelAndView("redirect:/adminUser/");
	}

	@GetMapping("/adminUser/{id}/edit")
	public ModelAndView edit(@PathVariable UUID id) {

		var adminUser = adminUserRepository.findById(id).get();
		return new ModelAndView("adminUser/edit", Map.of("adminUser", adminUser));
	}

	@PutMapping("/adminUser/{id}")
	@Transactional
	public ModelAndView update(@PathVariable UUID id, @Validated({ Default.class, Update.class }) AdminUser adminUser,
			BindingResult bindingResult) {

		var updatedAdminUser = adminUserRepository.findById(id).get();

		if (bindingResult.hasErrors()) {
			adminUser.setId(updatedAdminUser.getId());
			return new ModelAndView("adminUser/edit");
		}

		updatedAdminUser.setName(adminUser.getName());
		updatedAdminUser.setEnabled(adminUser.isEnabled());
		updatedAdminUser.setPlainPassword(adminUser.getPlainPassword());
		updatedAdminUser.setPasswordConfirmation(adminUser.getPasswordConfirmation());
		var passwordChanged = updatedAdminUser.changePassword(passwordEncoder);

		var beforeRole = updatedAdminUser.getRole();
		var afterRole = adminUser.getRole();
		updatedAdminUser.setRole(adminUser.getRole());

		adminUserRepository.save(updatedAdminUser);
		if (beforeRole != afterRole) {
			disableSession(updatedAdminUser.getUsername());
			logger.info("ユーザーのロールを変更しました。 Before=" + beforeRole + ", AdminUser=" + updatedAdminUser);
		}

		if (passwordChanged) {
			disableSession(updatedAdminUser.getUsername());
			logger.info("パスワードを変更しました。 AdminUser=" + updatedAdminUser);
		}

		return new ModelAndView("redirect:/adminUser/");
	}

	@GetMapping("/adminUser/{id}")
	public ModelAndView show(@PathVariable UUID id) {

		var adminUser = adminUserRepository.findById(id).get();

		return new ModelAndView("adminUser/show", Map.of("adminUser", adminUser));
	}

	@DeleteMapping("/adminUser/{id}")
	public ModelAndView delete(@PathVariable UUID id) {

		adminUserRepository.deleteById(id);
		logger.info("ユーザーを削除しました。 ID=" + id);
		return new ModelAndView("redirect:/adminUser/");
	}

	@InitBinder
	void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("name", "username", "plainPassword", "passwordConfirmation", "enabled", "role");
	}

	private void disableSession(String username) {

		var principal = findUserDetails(username);
		if (principal.isEmpty()) {
			return;
		}

		var sessionInfo = sessionRegistry.getAllSessions(principal.get(), false);
		sessionInfo.forEach(SessionInformation::expireNow);
	}

	private Optional<Object> findUserDetails(String username) {
		for (var principal : sessionRegistry.getAllPrincipals()) {
			if (principal instanceof UserDetails) {
				UserDetails user = (UserDetails) principal;
				if (user.getUsername().equals(username)) {
					return Optional.of(principal);
				}
			}
		}
		return Optional.empty();
	}

}
