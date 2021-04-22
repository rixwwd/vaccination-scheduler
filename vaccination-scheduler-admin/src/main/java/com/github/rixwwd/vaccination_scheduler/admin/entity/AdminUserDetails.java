package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 113315557443587550L;

	private final AdminUser adminUser;

	public AdminUserDetails(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return adminUser.getRole().getAuthorities();
	}

	@Override
	public String getPassword() {
		return adminUser.getPassword();
	}

	@Override
	public String getUsername() {
		return adminUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return adminUser.isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return !adminUser.isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return adminUser.isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return adminUser.isEnabled();
	}

}
