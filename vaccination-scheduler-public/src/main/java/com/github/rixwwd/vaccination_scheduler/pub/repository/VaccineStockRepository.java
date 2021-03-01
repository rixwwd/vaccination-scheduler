package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.VaccineStock;

@Repository
public interface VaccineStockRepository extends JpaRepository<VaccineStock, UUID> {

	Optional<VaccineStock> findByRoomId(UUID roomId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT v FROM VaccineStock v WHERE room_id = :roomId AND expected_delivery_date >= :date")
	List<VaccineStock> getByRoomIdAndGraterThanEqualExpectedDeliveryDateForWrite(@Param("roomId") UUID roomId,
			@Param("date") LocalDate date);

}
