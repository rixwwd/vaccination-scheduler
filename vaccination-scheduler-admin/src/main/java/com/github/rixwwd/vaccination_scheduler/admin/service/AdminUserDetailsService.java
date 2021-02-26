package com.github.rixwwd.vaccination_scheduler.admin.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.rixwwd.vaccination_scheduler.admin.repository.AdminUserRepository;

@Service
public class AdminUserDetailsService implements UserDetailsService {

	private AdminUserRepository adminUserRepository;

	public AdminUserDetailsService(AdminUserRepository adminUserRepository) {
		this.adminUserRepository = adminUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return adminUserRepository.findByUsername(username).orElseThrow(() -> {
			return new UsernameNotFoundException("Username not found. username=" + username);
		});
	}

}
