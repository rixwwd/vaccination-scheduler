package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.UUID;

import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Room;

@Component
public class RoomBeforeSaveCallback implements BeforeSaveCallback<Room> {

	@Override
	public Room onBeforeSave(Room aggregate, MutableAggregateChange<Room> aggregateChange) {
		if (aggregate.getId() == null) {
			aggregate.setId(UUID.randomUUID());
		}

		return aggregate;
	}
}
