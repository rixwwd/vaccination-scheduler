package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "PUBLIC_USERS")
@EntityListeners(AuditingEntityListener.class)
public class PublicUser {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "ID")
	private UUID id;

	@NotBlank
	@Size(max = 255)
	@Column(name = "LOGIN_NAME")
	private String loginName;

	@NotBlank
	@Size(max = 255)
	@Column(name = "PASSWORD")
	private String password;

	@NotBlank
	@Size(max = 255)
	@Column(name = "COUPON")
	private String coupon;

	@NotBlank
	@Size(max = 255)
	@Column(name = "NAME")
	private String name;

	@Size(max = 255)
	@Column(name = "HURIGANA")
	private String hurigana;

	@DateTimeFormat(pattern = "uuuu-MM-dd")
	@Column(name = "BIRTHDAY")
	private LocalDate birthday;

	@Size(max = 255)
	@Column(name = "ADDRESS")
	private String address;

	@Size(max = 255)
	@Column(name = "TELEPHONE_NUMBER")
	private String telephoneNumber;

	@Size(max = 255)
	@Column(name = "EMAIL")
	private String email;

	@Size(max = 255)
	@Column(name = "SMS")
	private String sms;

	@CreatedDate
	@Column
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column
	private LocalDateTime updatedAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHurigana() {
		return hurigana;
	}

	public void setHurigana(String hurigana) {
		this.hurigana = hurigana;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

}
