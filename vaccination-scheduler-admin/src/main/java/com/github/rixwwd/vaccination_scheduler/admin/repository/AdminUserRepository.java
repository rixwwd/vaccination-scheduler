package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.admin.entity.AdminUser;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, UUID> {

	Optional<AdminUser> findByUsername(String username);
}
