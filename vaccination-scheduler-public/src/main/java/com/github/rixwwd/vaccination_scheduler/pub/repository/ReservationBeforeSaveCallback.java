package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.UUID;

import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;

@Component
public class ReservationBeforeSaveCallback implements BeforeSaveCallback<Reservation> {

	@Override
	public Reservation onBeforeSave(Reservation aggregate, MutableAggregateChange<Reservation> aggregateChange) {
		if (aggregate.getId() == null) {
			aggregate.setId(UUID.randomUUID());
		}
		return aggregate;
	}

}
