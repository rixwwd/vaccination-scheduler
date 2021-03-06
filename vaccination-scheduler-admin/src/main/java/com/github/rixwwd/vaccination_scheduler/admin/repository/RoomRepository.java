package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

}
