package com.github.rixwwd.vaccination_scheduler.pub.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.rixwwd.vaccination_scheduler.pub.repository.WaitingListRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.CancelNoticeService;
import com.github.rixwwd.vaccination_scheduler.pub.service.NoopCancelNoticeService;

@Configuration
public class TestCancelNotificationConfiguration {

	@Bean
	public CancelNoticeService cancelNoticeService(WaitingListRepository waitingListRepository) {
		return new NoopCancelNoticeService(waitingListRepository);
	}

}
