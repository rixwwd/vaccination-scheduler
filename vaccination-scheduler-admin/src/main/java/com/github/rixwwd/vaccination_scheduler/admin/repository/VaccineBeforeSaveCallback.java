package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.UUID;

import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.stereotype.Component;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Vaccine;

@Component
public class VaccineBeforeSaveCallback implements BeforeSaveCallback<Vaccine> {

	@Override
	public Vaccine onBeforeSave(Vaccine aggregate, MutableAggregateChange<Vaccine> aggregateChange) {
		if (aggregate.getId() == null) {
			aggregate.setId(UUID.randomUUID());
		}

		return aggregate;
	}

}
