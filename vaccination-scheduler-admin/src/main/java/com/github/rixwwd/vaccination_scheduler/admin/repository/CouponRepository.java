package com.github.rixwwd.vaccination_scheduler.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Coupon;
import com.github.rixwwd.vaccination_scheduler.admin.entity.CouponPk;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, CouponPk> {

}
