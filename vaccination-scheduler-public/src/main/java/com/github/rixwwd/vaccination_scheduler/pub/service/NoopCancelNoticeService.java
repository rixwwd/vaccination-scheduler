package com.github.rixwwd.vaccination_scheduler.pub.service;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.repository.WaitingListRepository;

public class NoopCancelNoticeService implements CancelNoticeService {

	private final Logger logger = LoggerFactory.getLogger(NoopCancelNoticeService.class);

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm");

	private final WaitingListRepository waitingListRepository;

	public NoopCancelNoticeService(WaitingListRepository waitingListRepository) {
		this.waitingListRepository = waitingListRepository;
	}

	@Override
	public void notifyCancel(Cell cell) {

		var waitingList = waitingListRepository.findByCellIdOrderByCreatedAtAsc(cell.getId());
		for (var waiting : waitingList) {
			sendCancelNotice(waiting.getPublicUser(), waiting.getCell());
		}
	}

	@Override
	public void notifyAddWaitingList(PublicUser publicUser, Cell cell) {

		if (!logger.isInfoEnabled()) {
			return;
		}

		var text = new StringBuilder();
		text.append(publicUser.getName() + "様\r\n");
		text.append("COVID-19ワクチン接種のキャンセル待ちに登録しました。\r\n");
		text.append("場所: " + cell.getRoom().getName() + "\r\n");
		text.append("日時: " + cell.getBeginTime().format(formatter) + "\r\n");

		logger.info(text.toString());

	}

	private void sendCancelNotice(PublicUser publicUser, Cell cell) {
		if (!logger.isInfoEnabled()) {
			return;
		}

		var text = new StringBuilder();
		text.append(publicUser.getName() + "様\r\n");
		text.append("\r\n");
		text.append("COVID-19ワクチン接種の予約が可能になりました。\r\n");
		text.append("場所: " + cell.getRoom().getName() + "\r\n");
		text.append("日時: " + cell.getBeginTime().format(formatter) + "\r\n");

		logger.info(text.toString());
	}

}
