package com.github.rixwwd.vaccination_scheduler.admin.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "WAITING_LIST")
@EntityListeners(AuditingEntityListener.class)
public class WaitingList {

	@EmbeddedId
	private WaitingListPk waitingListPk;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CELL_ID", insertable = false, updatable = false)
	private Cell cell;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PUBLIC_USER_ID", insertable = false, updatable = false)
	private PublicUser publicUser;

	@CreatedDate
	@Column(name = "CREATED_AT")
	private LocalDateTime createdAt;

	public WaitingListPk getWaitingListPk() {
		return waitingListPk;
	}

	public void setWaitingListPk(WaitingListPk waitingListPk) {
		this.waitingListPk = waitingListPk;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
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
