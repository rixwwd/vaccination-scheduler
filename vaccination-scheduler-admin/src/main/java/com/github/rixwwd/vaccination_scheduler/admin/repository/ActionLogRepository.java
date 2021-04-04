package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rixwwd.vaccination_scheduler.admin.entity.ActionLog;

public interface ActionLogRepository extends JpaRepository<ActionLog, UUID> {

}
