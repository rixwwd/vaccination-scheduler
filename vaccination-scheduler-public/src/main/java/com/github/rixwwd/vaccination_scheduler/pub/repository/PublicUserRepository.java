package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;

@Repository
public interface PublicUserRepository extends JpaRepository<PublicUser, UUID> {

	Optional<PublicUser> findByLoginName(String logiName);

}
