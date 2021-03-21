package com.github.rixwwd.vaccination_scheduler.admin.dto;

import java.time.LocalDate;

public class VaccinationCount {

	private final LocalDate date;

	private final long count;

	public VaccinationCount(LocalDate date, long count) {
		this.date = date;
		this.count = count;
	}

	public LocalDate getDate() {
		return date;
	}

	public long getCount() {
		return count;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VaccinationCount [date=");
		builder.append(date);
		builder.append(", count=");
		builder.append(count);
		builder.append("]");
		return builder.toString();
	}

}
