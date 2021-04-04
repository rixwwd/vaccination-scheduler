package com.github.rixwwd.vaccination_scheduler.pub.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.entity.WaitingList;
import com.github.rixwwd.vaccination_scheduler.pub.entity.WaitingListPk;
import com.github.rixwwd.vaccination_scheduler.pub.exception.DuplicateWaitingException;
import com.github.rixwwd.vaccination_scheduler.pub.exception.NoContactException;
import com.github.rixwwd.vaccination_scheduler.pub.repository.WaitingListRepository;

@Service
public class WaitingListService {

	private WaitingListRepository waitingListRepository;

	public WaitingListService(WaitingListRepository waitingListRepository) {
		this.waitingListRepository = waitingListRepository;
	}

	@Transactional
	public void addWaitingList(PublicUser publicUser, Cell cell) throws NoContactException {

		if (publicUser.getEmail() == null || publicUser.getEmail().isEmpty()) {
			throw new NoContactException();
		}

		var waitingList = new WaitingList();
		waitingList.setCellId(cell.getId());
		waitingList.setPublicUserId(publicUser.getId());

		try {
			waitingListRepository.saveAndFlush(waitingList);
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateWaitingException();
		}
	}

	@Transactional
	public void delete(WaitingListPk pk) {
		waitingListRepository.deleteById(pk);
	}

}
