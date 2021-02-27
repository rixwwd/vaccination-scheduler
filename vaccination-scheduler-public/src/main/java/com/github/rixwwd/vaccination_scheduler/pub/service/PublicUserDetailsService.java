package com.github.rixwwd.vaccination_scheduler.pub.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;

@Service
public class PublicUserDetailsService implements UserDetailsService {

	private PublicUserRepository publicUserRepository;

	public PublicUserDetailsService(PublicUserRepository publicUserRepository) {
		this.publicUserRepository = publicUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return publicUserRepository.findByLoginName(username).orElseThrow(() -> {
			return new UsernameNotFoundException("login name not found. loginName=" + username);
		});
	}

}
