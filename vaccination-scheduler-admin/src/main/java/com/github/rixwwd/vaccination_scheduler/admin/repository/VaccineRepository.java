package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Vaccine;

@Repository
public interface VaccineRepository extends CrudRepository<Vaccine, UUID> {

}
