package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

	/**
	 * {@link PublicUser}を指定して、受付済みではない予約を取得する。
	 * 
	 * @param publicUserId {@link PublicUser}のID
	 * @return {@link Reservation}
	 */
	Optional<Reservation> findByPublicUserIdAndAcceptedIsFalse(UUID publicUserId);

	long countByCellId(UUID cellId);

}
