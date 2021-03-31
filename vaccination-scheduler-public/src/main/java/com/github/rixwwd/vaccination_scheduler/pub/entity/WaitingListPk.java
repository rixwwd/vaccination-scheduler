package com.github.rixwwd.vaccination_scheduler.pub.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WaitingListPk implements Serializable {

	private static final long serialVersionUID = -7285795375424470683L;

	@Column(name = "CELL_ID")
	private final UUID cellId;

	@Column(name = "PUBLIC_USER_ID")
	private final UUID publicUserId;

	public WaitingListPk() {
		this.cellId = null;
		this.publicUserId = null;
	}

	public WaitingListPk(UUID cellId, UUID publicUserId) {
		this.cellId = cellId;
		this.publicUserId = publicUserId;
	}

	public UUID getCellId() {
		return cellId;
	}

	public UUID getPublicUserId() {
		return publicUserId;
	}

}
