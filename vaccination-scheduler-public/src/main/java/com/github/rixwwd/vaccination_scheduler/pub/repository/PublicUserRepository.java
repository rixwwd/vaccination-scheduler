package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;

@Repository
public interface PublicUserRepository extends CrudRepository<PublicUser, UUID> {

	Optional<PublicUser> findByLoginName(String logiName);

}
