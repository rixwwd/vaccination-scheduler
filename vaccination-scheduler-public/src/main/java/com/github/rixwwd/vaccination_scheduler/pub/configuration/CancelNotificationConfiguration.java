package com.github.rixwwd.vaccination_scheduler.pub.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.github.rixwwd.vaccination_scheduler.pub.repository.WaitingListRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.CancelNoticeService;
import com.github.rixwwd.vaccination_scheduler.pub.service.NoopCancelNoticeService;
import com.github.rixwwd.vaccination_scheduler.pub.service.SmtpCancelNoticeService;

@Configuration
public class CancelNotificationConfiguration {

	@Bean
	@ConditionalOnBean(JavaMailSender.class)
	public CancelNoticeService smtpCancelNoticeService(JavaMailSender mailSender,
			WaitingListRepository waitingListRepository) {
		return new SmtpCancelNoticeService(mailSender, waitingListRepository);
	}

	@Bean
	@ConditionalOnMissingBean(JavaMailSender.class)
	public CancelNoticeService noopCancelNoticeService(WaitingListRepository waitingListRepository) {
		return new NoopCancelNoticeService(waitingListRepository);
	}

}
