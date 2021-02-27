package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.VaccinationHistory;

@Repository
public interface VaccinationHistoryRepository extends CrudRepository<VaccinationHistory, UUID> {

}
