package com.github.rixwwd.vaccination_scheduler.pub.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Room;
import com.github.rixwwd.vaccination_scheduler.pub.entity.VaccineStock;

@Repository
public interface VaccineStockRepository extends JpaRepository<VaccineStock, UUID> {

	Optional<VaccineStock> findByRoomId(UUID roomId);

	/**
	 * 指定日までに配送されていて、在庫のあるワクチン(予約可能なワクチン)を取得する。
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
			+ " AND reservation_count < quantity"
			+ " AND vaccinated_count < quantity")
	//@formatter:on
	List<VaccineStock> findInStockForWrite(@Param("roomId") UUID roomId, @Param("date") LocalDate date);

	/**
	 * 指定日までに配送されていて、使い切ってないワクチン(キャンセル可能なワクチン)を取得する。
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
			+ " AND reservation_count > 0"
			+ " AND vaccinated_count < quantity")
	//@formatter:on
	List<VaccineStock> findCancelableForWrite(@Param("roomId") UUID roomId, @Param("date") LocalDate date);

}
