package com.github.rixwwd.vaccination_scheduler.pub.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.github.rixwwd.vaccination_scheduler.pub.entity.ActionType;
import com.github.rixwwd.vaccination_scheduler.pub.repository.PublicUserRepository;
import com.github.rixwwd.vaccination_scheduler.pub.service.ActionLogService;

@Component
public class ActionLogHandler {

	private final ActionLogService actionLogService;

	private final PublicUserRepository publicUserRepository;

	public ActionLogHandler(ActionLogService actionLogService, PublicUserRepository publicUserRepository) {
		this.actionLogService = actionLogService;
		this.publicUserRepository = publicUserRepository;
	}

	@EventListener
	public void onSuccess(AuthenticationSuccessEvent event) {

		var authentication = event.getAuthentication();
		var publicUser = publicUserRepository
				.findByLoginName(((UserDetails) authentication.getPrincipal()).getUsername()).orElseThrow();

		actionLogService.log(publicUser, ActionType.LOGIN);
	}
}
