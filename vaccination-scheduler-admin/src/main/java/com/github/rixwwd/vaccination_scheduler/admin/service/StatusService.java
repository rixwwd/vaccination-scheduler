package com.github.rixwwd.vaccination_scheduler.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.rixwwd.vaccination_scheduler.admin.dto.VaccinationCount;
import com.github.rixwwd.vaccination_scheduler.admin.repository.VaccinationHistoryRepository;

@Service
public class StatusService {

	private VaccinationHistoryRepository vaccinationHistoryRepository;

	public StatusService(VaccinationHistoryRepository vaccinationHistoryRepository) {
		this.vaccinationHistoryRepository = vaccinationHistoryRepository;
	}

	public List<VaccinationCount> countDailyVaccination() {
		return vaccinationHistoryRepository.countGroupByVaccinatedAt();

	}
}
