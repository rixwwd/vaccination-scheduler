package com.github.rixwwd.vaccination_scheduler.pub.service;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.exception.InvalidPasswordException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;

@Service
public class PasswordService {

	private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	private final PublicUserRepository publicUserRepository;

	public PasswordService(PublicUserRepository publicUserRepository) {
		this.publicUserRepository = publicUserRepository;
	}

	@Transactional
	public void changePassword(PublicUser publicUser, String currentPassword, String newPassword)
			throws InvalidPasswordException {

		if (!passwordEncoder.matches(currentPassword, publicUser.getPassword())) {
			throw new InvalidPasswordException();
		}

		publicUser.setPassword(passwordEncoder.encode(newPassword));
		publicUserRepository.save(publicUser);
	}
}
