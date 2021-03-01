package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.admin.entity.VaccineStock;

@Repository
public interface VaccineStockRepository extends JpaRepository<VaccineStock, UUID> {

}
