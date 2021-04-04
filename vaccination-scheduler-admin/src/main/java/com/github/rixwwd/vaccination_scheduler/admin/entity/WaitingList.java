package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "WAITING_LIST")
@EntityListeners(AuditingEntityListener.class)
@IdClass(WaitingListPk.class)
public class WaitingList {

	@Id
	@Column(name = "CELL_ID")
	private UUID cellId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CELL_ID", insertable = false, updatable = false)
	private Cell cell;

	@Id
	@Column(name = "PUBLIC_USER_ID")
	private UUID publicUserId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PUBLIC_USER_ID", insertable = false, updatable = false)
	private PublicUser publicUser;

	@CreatedDate
	@Column(name = "CREATED_AT")
	private LocalDateTime createdAt;

	public UUID getCellId() {
		return cellId;
	}

	public void setCellId(UUID cellId) {
		this.cellId = cellId;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public UUID getPublicUserId() {
		return publicUserId;
	}

	public void setPublicUserId(UUID publicUserId) {
		this.publicUserId = publicUserId;
	}

	public PublicUser getPublicUser() {
		return publicUser;
	}

	public void setPublicUser(PublicUser publicUser) {
		this.publicUser = publicUser;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
