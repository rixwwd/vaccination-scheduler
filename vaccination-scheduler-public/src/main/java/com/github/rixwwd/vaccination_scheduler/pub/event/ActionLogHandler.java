package com.github.rixwwd.vaccination_scheduler.pub.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.github.rixwwd.vaccination_scheduler.pub.entity.ActionType;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.service.ActionLogService;

@Component
public class ActionLogHandler {

	private final ActionLogService actionLogService;

	public ActionLogHandler(ActionLogService actionLogService) {
		this.actionLogService = actionLogService;
	}

	@EventListener
	public void onSuccess(AuthenticationSuccessEvent event) {

		var authentication = event.getAuthentication();
		actionLogService.log((PublicUser) authentication.getPrincipal(), ActionType.LOGIN);
	}
}
