package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {

	ADMIN("システム管理者", new SimpleGrantedAuthority("ADMIN_USER")),

	VACCINE_ADMIN("ワクチン管理者", new SimpleGrantedAuthority("ROOM"), new SimpleGrantedAuthority("CELL"),
			new SimpleGrantedAuthority("VACCINE"), new SimpleGrantedAuthority("PUBLIC_USER"),
			new SimpleGrantedAuthority("VACCINE_STOCK")),

	RECEPTIONIST("受付担当", new SimpleGrantedAuthority("ACCEPTANCE"), new SimpleGrantedAuthority("VACCINATION"));

	private final List<GrantedAuthority> authorities;

	private final String name;

	private Role(String name, GrantedAuthority... authorities) {
		this.name = name;
		this.authorities = Arrays.asList(authorities);
	}

	public String getRoleName() {
		return name;
	}

	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}
}
