package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Room;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Vaccine;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

	List<Room> findByVaccine(Vaccine vaccine);
}
