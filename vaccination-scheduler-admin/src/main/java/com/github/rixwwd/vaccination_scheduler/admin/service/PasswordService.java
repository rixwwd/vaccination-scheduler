package com.github.rixwwd.vaccination_scheduler.admin.service;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.admin.entity.AdminUser;
import com.github.rixwwd.vaccination_scheduler.admin.exception.InvalidPasswordException;
import com.github.rixwwd.vaccination_scheduler.admin.repository.AdminUserRepository;

@Service
public class PasswordService {

	private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	private final AdminUserRepository adminUserRepository;

	public PasswordService(AdminUserRepository adminUserRepository) {
		this.adminUserRepository = adminUserRepository;
	}

	@Transactional
	public void changePassword(AdminUser adminUser, String currentPassword, String newPassword)
			throws InvalidPasswordException {

		if (!passwordEncoder.matches(currentPassword, adminUser.getPassword())) {
			throw new InvalidPasswordException();
		}

		adminUser.setPassword(passwordEncoder.encode(newPassword));
		adminUserRepository.save(adminUser);
	}
}
