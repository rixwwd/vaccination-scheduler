package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

@Entity
@Table(name = "PUBLIC_USERS")
@EntityListeners({ AuditingEntityListener.class, PasswordEncodeListener.class })
public class PublicUser implements PasswordEncodable {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "ID")
	private UUID id;

	@NotBlank
	@Size(max = 255)
	@Column(name = "LOGIN_NAME")
	private String loginName;

	@Column(name = "PASSWORD")
	private String password;

	@NotBlank(groups = { Create.class, CreateFromCSV.class })
	@Size(max = 255, groups = { Create.class, UpdatePassword.class, CreateFromCSV.class })
	@Transient
	private String plainPassword;

	@NotBlank(groups = Create.class)
	@Size(max = 255, groups = { Create.class, UpdatePassword.class })
	@Transient
	private String plainPasswordConfirmation;

	@NotBlank
	@Size(max = 255)
	@Column(name = "NAME")
	private String name;

	@Size(max = 255)
	@Column(name = "HURIGANA")
	private String hurigana;

	@DateTimeFormat(pattern = "uuuu-MM-dd")
	@Column(name = "BIRTHDAY")
	@JsonFormat(pattern = "uuuu-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
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

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "publicUserId")
	@Valid
	private Set<Coupon> coupons;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "publicUserId", orphanRemoval = true)
	private Set<Reservation> reservations = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "publicUserId")
	private Set<WaitingList> waitingList = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "publicUserId")
	private Set<VaccinationHistory> vaccinationHistories = new HashSet<>();

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

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPlainPasswordConfirmation() {
		return plainPasswordConfirmation;
	}

	public void setPlainPasswordConfirmation(String plainPasswordConfirmation) {
		this.plainPasswordConfirmation = plainPasswordConfirmation;
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

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	public Set<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(Set<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Set<WaitingList> getWaitingList() {
		return waitingList;
	}

	public void setWaitingList(Set<WaitingList> waitingList) {
		this.waitingList = waitingList;
	}

	public Set<VaccinationHistory> getVaccinationHistories() {
		return vaccinationHistories;
	}

	public void setVaccinationHistories(Set<VaccinationHistory> vaccinationHistories) {
		this.vaccinationHistories = vaccinationHistories;
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

	public Optional<VaccinationHistory> getFirstVaccinationHistory() {
		return vaccinationHistories.stream().sorted((a, b) -> a.getVaccinatedAt().compareTo(b.getVaccinatedAt()))
				.findFirst();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PublicUser [id=");
		builder.append(id);
		builder.append(", loginName=");
		builder.append(loginName);
		builder.append(", password=");
		builder.append(password);
		builder.append(", plainPassword=");
		builder.append(plainPassword);
		builder.append(", plainPasswordConfirmation=");
		builder.append(plainPasswordConfirmation);
		builder.append(", name=");
		builder.append(name);
		builder.append(", hurigana=");
		builder.append(hurigana);
		builder.append(", birthday=");
		builder.append(birthday);
		builder.append(", address=");
		builder.append(address);
		builder.append(", telephoneNumber=");
		builder.append(telephoneNumber);
		builder.append(", email=");
		builder.append(email);
		builder.append(", sms=");
		builder.append(sms);
		builder.append(", coupons=");
		builder.append(coupons);
		builder.append(", reservations=");
		builder.append(reservations);
		builder.append(", waitingList=");
		builder.append(waitingList);
		builder.append(", vaccinationHistories=");
		builder.append(vaccinationHistories);
		builder.append(", createdAt=");
		builder.append(createdAt);
		builder.append(", updatedAt=");
		builder.append(updatedAt);
		builder.append("]");
		return builder.toString();
	}

	public static interface Create {
	}

	public static interface UpdatePassword {
	}

	public static interface CreateFromCSV {
	}

}
