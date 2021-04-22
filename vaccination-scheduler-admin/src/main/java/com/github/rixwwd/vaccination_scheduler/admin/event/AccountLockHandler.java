package com.github.rixwwd.vaccination_scheduler.admin.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.admin.repository.AdminUserRepository;

@Component
public class AccountLockHandler {

	private final AdminUserRepository adminUserRepository;

	public AccountLockHandler(AdminUserRepository adminUserRepository) {
		this.adminUserRepository = adminUserRepository;
	}

	@EventListener
	@Transactional
	public void onFailure(AuthenticationFailureBadCredentialsEvent event) {

		var token = (UsernamePasswordAuthenticationToken) event.getSource();
		var adminUser = adminUserRepository.findByUsername(token.getName());
		if (adminUser.isEmpty()) {
			return;
		}

		var targetAdminUser = adminUser.get();
		targetAdminUser.loginfailed();
		adminUserRepository.save(targetAdminUser);
	}

	@EventListener
	@Transactional
	public void onSuccess(AuthenticationSuccessEvent event) {

		var token = (UsernamePasswordAuthenticationToken) event.getSource();
		var adminUser = adminUserRepository.findByUsername(token.getName());
		if (adminUser.isEmpty()) {
			return;
		}

		var targetAdminUser = adminUser.get();
		targetAdminUser.resetFailedLoginCount();
		adminUserRepository.save(targetAdminUser);
	}
}
