package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.admin.dto.VaccinationCount;
import com.github.rixwwd.vaccination_scheduler.admin.entity.VaccinationHistory;

@Repository
public interface VaccinationHistoryRepository extends JpaRepository<VaccinationHistory, UUID> {

	//@formatter:off
	@Query("select new com.github.rixwwd.vaccination_scheduler.admin.dto.VaccinationCount(vh.vaccinatedAt, count(*)) "
			+ "from VaccinationHistory vh "
			+ "group by vaccinatedAt "
			+ "order by vaccinatedAt asc")
	//@formatter:on
	List<VaccinationCount> countGroupByVaccinatedAt();

	List<VaccinationHistory> findByPublicUserIdOrderByVaccinatedAtAsc(UUID publicUserId);
}
