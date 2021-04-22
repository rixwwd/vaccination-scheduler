package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.rixwwd.vaccination_scheduler.admin.validator.Confirmation;

@Entity
@Table(name = "ADMIN_USERS")
@EntityListeners(AuditingEntityListener.class)
@Confirmation(field = "plainPassword", confirmationField = "passwordConfirmation", groups = { AdminUser.Create.class,
		AdminUser.Update.class })
public class AdminUser {

	private static final int LOCK_COUNT = 5;

	private static final Duration UNLOCK_TIME = Duration.ofHours(24);

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "ID")
	private UUID id;

	@NotBlank(groups = Create.class)
	@Pattern(regexp = "[a-z0-9_]{4,255}", groups = Create.class)
	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Transient
	@NotBlank(groups = Create.class)
	@Size(min = 8, groups = { Create.class, Update.class })
	private String plainPassword;

	@Transient
	@NotBlank(groups = Create.class)
	@Size(min = 8, groups = { Create.class, Update.class })
	private String passwordConfirmation;

	@Column(name = "ENABLED")
	private boolean enabled;

	@Enumerated(EnumType.STRING)
	@Column(name = "ROLE")
	@NotNull
	private Role role;

	@Size(max = 255)
	@NotBlank
	@Column(name = "NAME")
	private String name;

	@Column(name = "FAILED_LOGIN_COUNT")
	private int failedLoginCount;

	@Column(name = "LAST_FAILED_LOGIN")
	private LocalDateTime lastFailedLogin;

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

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFailedLoginCount() {
		return failedLoginCount;
	}

	public void setFailedLoginCount(int failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}

	public LocalDateTime getLastFailedLogin() {
		return lastFailedLogin;
	}

	public void setLastFailedLogin(LocalDateTime lastFailedLogin) {
		this.lastFailedLogin = lastFailedLogin;
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

	public boolean changePassword(PasswordEncoder encoder) {

		if (plainPassword != null && !plainPassword.isBlank() && plainPassword.equals(passwordConfirmation)) {
			password = encoder.encode(plainPassword);
			return true;
		}

		return false;
	}

	public void loginfailed() {
		failedLoginCount++;
		lastFailedLogin = LocalDateTime.now();
	}

	public void resetFailedLoginCount() {
		failedLoginCount = 0;
		lastFailedLogin = null;
	}

	public boolean isLocked() {

		final boolean overFailCount = failedLoginCount >= LOCK_COUNT;
		final boolean inLockTime = lastFailedLogin != null
				&& LocalDateTime.now().isBefore(lastFailedLogin.plus(UNLOCK_TIME));
		return overFailCount && inLockTime;
	}

	public static interface Create {
	}

	public static interface Update {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdminUser [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", enabled=");
		builder.append(enabled);
		builder.append(", role=");
		builder.append(role);
		builder.append(", name=");
		builder.append(name);
		builder.append(", createdAt=");
		builder.append(createdAt);
		builder.append(", updatedAt=");
		builder.append(updatedAt);
		builder.append("]");
		return builder.toString();
	}

}
