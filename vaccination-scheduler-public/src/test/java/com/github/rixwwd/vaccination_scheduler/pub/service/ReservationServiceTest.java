package com.github.rixwwd.vaccination_scheduler.pub.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Coupon;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.pub.entity.VaccineStock;
import com.github.rixwwd.vaccination_scheduler.pub.repository.CellRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.ReservationRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.VaccinationHistoryRepository;
import com.github.rixwwd.vaccination_scheduler.pub.repository.VaccineStockRepository;

@SpringBootTest
class ReservationServiceTest {

	@Autowired
	private ReservationService reservationService;

	@MockBean
	private CellRepository cellRepository;

	@MockBean
	private ReservationRepository reservationRepository;

	@MockBean
	private VaccineStockRepository vaccineStockRepository;

	@MockBean
	private VaccinationHistoryRepository vaccinationHistoryRepository;

	@Test
	void testReserve() {

		var cellId = UUID.fromString("3b730ff1-cbda-4eeb-83a1-e61ab9c071a7");
		var roomId = UUID.fromString("c182a523-1256-4621-b3d8-81d96d16f237");

		var cell = new Cell() {
			@Override
			public boolean isStarted() {
				return false;
			}
		};
		cell.setId(cellId);
		cell.setCapacity(1);
		cell.setRoomId(roomId);
		cell.setBeginTime(LocalDateTime.now());

		when(cellRepository.findByIdForWrite(Mockito.any(UUID.class))).thenReturn(Optional.of(cell));

		var stock = new VaccineStock();
		stock.setQuantity(1);
		when(vaccineStockRepository.findInStockForWrite(roomId, LocalDate.now())).thenReturn(Arrays.asList(stock));

		when(reservationRepository.countByCellId(cellId)).thenReturn(0L);

		var reservation = new Reservation();
		reservation.setCellId(cellId);
		reservation.setCoupon("12345");

		when(reservationRepository.saveAndFlush(reservation)).thenAnswer(new Answer<Reservation>() {

			@Override
			public Reservation answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgument(0);
			}
		});

		var publicUser = new PublicUser();
		var coupon = new Coupon();
		coupon.setCoupon("12345");
		coupon.setUsed(false);
		publicUser.setCoupons(List.of(coupon));
		var actualReservation = reservationService.reserve(reservation, publicUser);

		assertNotNull(actualReservation.getReservationNumber());
	}

}
