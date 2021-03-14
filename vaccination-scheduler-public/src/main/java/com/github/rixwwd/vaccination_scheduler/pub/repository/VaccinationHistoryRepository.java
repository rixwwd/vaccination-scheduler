package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.VaccinationHistory;

@Repository
public interface VaccinationHistoryRepository extends JpaRepository<VaccinationHistory, UUID> {

	List<VaccinationHistory> findByPublicUserIdOrderByVaccinatedAtAsc(UUID publicUserId);

}
