package com.github.rixwwd.vaccination_scheduler.pub.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.rixwwd.vaccination_scheduler.pub.repository.ActionLogRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.ActionLogService;
import com.github.rixwwd.vaccination_scheduler.pub.service.DbActionLogService;
import com.github.rixwwd.vaccination_scheduler.pub.service.Slf4jActionLogService;

@Configuration
public class ActionLogConfiguration {

	@Bean
	@ConditionalOnProperty(name = "app.action-log.enabled", havingValue = "true")
	public ActionLogService dbActionLogService(ActionLogRepository actionLogRepository) {
		return new DbActionLogService(actionLogRepository);
	}

	@Bean
	@ConditionalOnMissingBean(ActionLogService.class)
	public ActionLogService slf4jActionLogService() {
		return new Slf4jActionLogService();
	}
}
