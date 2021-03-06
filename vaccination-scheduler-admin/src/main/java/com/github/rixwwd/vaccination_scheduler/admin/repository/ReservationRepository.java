package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

	Optional<Reservation> findByReservationNumber(String reservationNumber);
}
