package com.github.rixwwd.vaccination_scheduler.pub.web;

import java.util.UUID;

import javax.validation.constraints.NotNull;

class WaitingListForm {

	@NotNull
	private UUID cellId;

	public UUID getCellId() {
		return cellId;
	}

	public void setCellId(UUID cellId) {
		this.cellId = cellId;
	}

}
