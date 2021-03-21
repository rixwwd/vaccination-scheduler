package com.github.rixwwd.vaccination_scheduler.admin.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.admin.entity.Room;
import com.github.rixwwd.vaccination_scheduler.admin.entity.VaccineStock;

@Repository
public interface VaccineStockRepository extends JpaRepository<VaccineStock, UUID> {

	/**
	 * 指定日までに配送されていて、未使用のワクチンを取得する。
	 * 
	 * @param roomId {@link Room}のID
	 * @param date   配送予定日
	 * @return {@link VaccineStock}
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	//@formatter:off
	@Query("SELECT v FROM VaccineStock v"
			+ " WHERE room_id = :roomId"
			+ " AND expected_delivery_date <= :date"
			+ " AND vaccinated_count < quantity")
	//@formatter:on
	List<VaccineStock> findUnusedStockForWrite(@Param("roomId") UUID roomId, @Param("date") LocalDate date);

}
