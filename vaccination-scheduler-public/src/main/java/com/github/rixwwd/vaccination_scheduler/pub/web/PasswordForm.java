package com.github.rixwwd.vaccination_scheduler.pub.web;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

class PasswordForm {

	@NotBlank
	@Size(max = 255)
	private String password;

	@NotBlank
	@Size(min = 8, max = 255)
	private String newPassword;

	@NotBlank
	@Size(min = 8, max = 255)
	private String newPasswordConfirmation;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirmation() {
		return newPasswordConfirmation;
	}

	public void setNewPasswordConfirmation(String newPasswordConfirmation) {
		this.newPasswordConfirmation = newPasswordConfirmation;
	}

	public boolean isMatchPassword() {
		return newPassword != null && !newPassword.isEmpty() && newPassword.equals(newPasswordConfirmation);
	}
}
