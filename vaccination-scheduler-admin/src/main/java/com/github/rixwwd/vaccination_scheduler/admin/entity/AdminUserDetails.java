package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 113315557443587550L;

	private final String username;

	private final String password;

	private final boolean enabled;

	public AdminUserDetails(String username, String password, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROOM"), new SimpleGrantedAuthority("CELL"),
				new SimpleGrantedAuthority("VACCINE"), new SimpleGrantedAuthority("ADMIN_USER"),
				new SimpleGrantedAuthority("PUBLIC_USER"), new SimpleGrantedAuthority("VACCINE_STOCK"),
				new SimpleGrantedAuthority("ACCEPTANCE"), new SimpleGrantedAuthority("VACCINATION"));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return enabled;
	}

	@Override
	public boolean isAccountNonLocked() {
		return enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
