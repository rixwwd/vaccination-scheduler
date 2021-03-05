package com.github.rixwwd.vaccination_scheduler.admin.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncodeListener {

	private PasswordEncoder encoder = new BCryptPasswordEncoder();

	@PrePersist
	@PreUpdate
	public void encodePlainPassword(Object target) {

		if (!(target instanceof PasswordEncodable)) {
			return;
		}

		var encodable = (PasswordEncodable) target;

		if (encodable.getPlainPassword() != null && !encodable.getPlainPassword().isBlank()) {
			encodable.setPassword("{bcrypt}" + encoder.encode(encodable.getPlainPassword()));
		}
	}
}
