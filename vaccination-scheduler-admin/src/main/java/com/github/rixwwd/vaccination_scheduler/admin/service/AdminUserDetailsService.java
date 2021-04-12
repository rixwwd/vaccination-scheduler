package com.github.rixwwd.vaccination_scheduler.admin.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.rixwwd.vaccination_scheduler.admin.entity.AdminUserDetails;
import com.github.rixwwd.vaccination_scheduler.admin.repository.AdminUserRepository;

@Service
public class AdminUserDetailsService implements UserDetailsService {

	private final AdminUserRepository adminUserRepository;

	public AdminUserDetailsService(AdminUserRepository adminUserRepository) {
		this.adminUserRepository = adminUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		var adminUser = adminUserRepository.findByUsername(username).orElseThrow(() -> {
			return new UsernameNotFoundException("Username not found. username=" + username);
		});

		return new AdminUserDetails(adminUser.getUsername(), adminUser.getPassword(), adminUser.isEnabled());
	}

}
