package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.WaitingList;
import com.github.rixwwd.vaccination_scheduler.pub.entity.WaitingListPk;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, WaitingListPk> {

	List<WaitingList> findByCellIdOrderByCreatedAtAsc(UUID cellId);

}
