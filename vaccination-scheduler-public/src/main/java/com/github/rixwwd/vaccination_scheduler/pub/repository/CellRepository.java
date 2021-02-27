package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;

@Repository
public interface CellRepository extends CrudRepository<Cell, UUID> {

	Iterable<Cell> findByRoomId(UUID roomId);
}
