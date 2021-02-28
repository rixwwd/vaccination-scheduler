package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "ADMIN_USERS")
@EntityListeners(AuditingEntityListener.class)
public class AdminUser implements UserDetails {

	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = -2335961635167529588L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "ID")
	private UUID id;

	@NotBlank
	@Pattern(regexp = "[a-z0-9_]{4, 255}")
	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "ENABLED")
	private boolean enabled;

	@Size(max = 255)
	@NotBlank
	@Column(name = "NAME")
	private String name;

	@CreatedDate
	@Column(name = "CREATED_AT")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "UPDATED_AT")
	private LocalDateTime updatedAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROOM"), new SimpleGrantedAuthority("CELL"),
				new SimpleGrantedAuthority("VACCINE"), new SimpleGrantedAuthority("ADMIN_USER"),
				new SimpleGrantedAuthority("PUBLIC_USER"), new SimpleGrantedAuthority("VACCINE_HISTORY"),
				new SimpleGrantedAuthority("RESERVATION"));
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.enabled;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.enabled;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.enabled;
	}

}
