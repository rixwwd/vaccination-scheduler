package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;

@Repository
public interface CellRepository extends JpaRepository<Cell, UUID> {

	Iterable<Cell> findByRoomId(UUID roomId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Cell c WHERE id = :id")
	Optional<Cell> findByIdForWrite(@Param("id") UUID id);
}
