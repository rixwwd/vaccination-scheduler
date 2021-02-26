package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.UUID;

import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Cell;

@Component
public class CellBeforeSaveCallback implements BeforeSaveCallback<Cell> {

	@Override
	public Cell onBeforeSave(Cell aggregate, MutableAggregateChange<Cell> aggregateChange) {
		if (aggregate.getId() == null) {
			aggregate.setId(UUID.randomUUID());
		}

		return aggregate;
	}
}
