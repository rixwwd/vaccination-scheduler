package com.github.rixwwd.vaccination_scheduler.pub.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Reservation;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Room;
import com.github.rixwwd.vaccination_scheduler.pub.entity.VaccinationHistory;
import com.github.rixwwd.vaccination_scheduler.pub.entity.Vaccine;
import com.github.rixwwd.vaccination_scheduler.pub.entity.WaitingList;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

	@MockBean
	private PublicUserRepository publicUserRepository;

	@MockBean
	private UserDetailsService userDetailsServcice;

	private PublicUser publicUser;

	@BeforeEach
	void beforeEach() {

		publicUser = new PublicUser();
		publicUser.setLoginName("user");

		var room = new Room();
		room.setName("test room");

		var cell1 = new Cell();
		cell1.setRoom(room);
		cell1.setBeginTime(LocalDateTime.now());

		var r1 = new Reservation();
		r1.setCell(cell1);

		var cell2 = new Cell();
		cell2.setRoom(room);
		cell2.setBeginTime(LocalDateTime.now());

		var wl = new WaitingList();
		wl.setCell(cell2);
		wl.setPublicUser(publicUser);

		var history = new VaccinationHistory();
		history.setPublicUser(publicUser);
		history.setRoomId(room.getId());
		history.setVaccine(Vaccine.PFIZER);

		publicUser.setReservations(Set.of(r1));
		publicUser.setWaitingList(Set.of(wl));
		publicUser.setVaccinationHistories(Set.of(history));

		Mockito.when(userDetailsServcice.loadUserByUsername("user")).thenReturn(publicUser);

	}

	@Test
	@WithUserDetails(setupBefore = TestExecutionEvent.TEST_EXECUTION)
	void testIndex(@Autowired MockMvc mvc) throws Exception {

		Mockito.when(publicUserRepository.findByLoginName("user")).thenReturn(Optional.of(publicUser));

		mvc.perform(get("/menu/")).andExpect(status().isOk());
	}

}
