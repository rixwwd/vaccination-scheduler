package com.github.rixwwd.vaccination_scheduler.pub.web;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

class ContactForm {

	@Size(max = 255)
	private String telephoneNumber;

	@Size(max = 255)
	@Email
	private String email;

	@Size(max = 255)
	private String sms;

	public ContactForm() {

	}

	public ContactForm(String telephoneNumber, String email, String sms) {
		this.telephoneNumber = telephoneNumber;
		this.email = email;
		this.sms = sms;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

}
