package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

	Optional<Reservation> findByPublicUserId(UUID publicUserId);

	long countByCellId(UUID cellId);

	@Query(value = "SELECT count(*) FROM reservations r LEFT JOIN cells c ON r.cell_id = c.id WHERE c.room_id = :roomId", nativeQuery = true)
	long countTodayReservationByRoomId(@Param("roomId") UUID roomId);
}
