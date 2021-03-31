package com.github.rixwwd.vaccination_scheduler.pub.service;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.repository.WaitingListRepository;

public class SmtpCancelNoticeService implements CancelNoticeService {

	private final Logger logger = LoggerFactory.getLogger(SmtpCancelNoticeService.class);

	private final JavaMailSender mailSender;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm");

	private final WaitingListRepository waitingListRepository;

	public SmtpCancelNoticeService(JavaMailSender mailSender, WaitingListRepository waitingListRepository) {
		this.mailSender = mailSender;
		this.waitingListRepository = waitingListRepository;
	}

	@Override
	public void notifyCancel(Cell cell) {

		var waitingList = waitingListRepository.findByCellIdOrderByCreatedAtAsc(cell.getId());

		logger.info("送信対象の件数=" + waitingList.size());
		for (var waiting : waitingList) {
			sendCancelNotice(waiting.getPublicUser(), waiting.getCell());
			logger.info("送信完了 PublicUser=" + waiting.getPublicUser().getId());
		}
	}

	@Override
	public void notifyAddWaitingList(PublicUser publicUser, Cell cell) {
		var message = new SimpleMailMessage();
		// FIXME 送信元のメールアドレスは設定で変えられるようにする。
		message.setFrom("no-reply@example.com");
		message.setTo(publicUser.getEmail());
		message.setSubject("キャンセル待ち登録完了");

		var text = new StringBuilder();
		text.append("COVID-19ワクチン接種のキャンセル待ちに登録しました。\r\n");
		text.append("場所: " + cell.getRoom().getName() + "\r\n");
		text.append("日時: " + cell.getBeginTime().format(formatter) + "\r\n");

		message.setText(text.toString());

		mailSender.send(message);

	}

	private void sendCancelNotice(PublicUser publicUser, Cell cell) {
		var message = new SimpleMailMessage();
		message.setFrom("no-reply@example.com");
		message.setTo(publicUser.getEmail());
		message.setSubject("ワクチン接種の予約にキャンセルが発生しました");

		var text = new StringBuilder();
		text.append("COVID-19ワクチン接種の予約が可能になりました。\r\n");
		text.append("場所: " + cell.getRoom().getName() + "\r\n");
		text.append("日時: " + cell.getBeginTime().format(formatter) + "\r\n");

		message.setText(text.toString());

		mailSender.send(message);
	}

}
