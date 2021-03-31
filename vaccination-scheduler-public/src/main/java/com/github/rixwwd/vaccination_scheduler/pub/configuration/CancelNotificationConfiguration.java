package com.github.rixwwd.vaccination_scheduler.pub.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.github.rixwwd.vaccination_scheduler.pub.repository.WaitingListRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.CancelNoticeService;
import com.github.rixwwd.vaccination_scheduler.pub.service.SmtpCancelNoticeService;

@Configuration
public class CancelNotificationConfiguration {

	@Bean
	public CancelNoticeService cancelNoticeService(JavaMailSender mailSender,
			WaitingListRepository waitingListRepository) {
		return new SmtpCancelNoticeService(mailSender, waitingListRepository);
	}

}
