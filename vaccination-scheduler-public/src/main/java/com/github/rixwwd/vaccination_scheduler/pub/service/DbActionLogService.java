package com.github.rixwwd.vaccination_scheduler.pub.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.rixwwd.vaccination_scheduler.pub.entity.ActionLog;
import com.github.rixwwd.vaccination_scheduler.pub.entity.ActionType;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;
import com.github.rixwwd.vaccination_scheduler.pub.repository.ActionLogRepository;

public class DbActionLogService implements ActionLogService {

	private final ActionLogRepository actionLogRepository;

	public DbActionLogService(ActionLogRepository actionLogRepository) {
		this.actionLogRepository = actionLogRepository;
	}

	@Override
	@Transactional
	public void log(PublicUser publicUser, ActionType actionType) {

		var actionLog = new ActionLog();
		actionLog.setPublicUserId(publicUser.getId());
		actionLog.setActionType(actionType);
		actionLog.setIpAddress(getRemoteAddress());
		actionLogRepository.save(actionLog);
	}

	protected String getRemoteAddress() {
		var requestAttibutes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		if (requestAttibutes != null) {
			var httpServletRequest = requestAttibutes.getRequest();
			if (httpServletRequest != null) {
				return httpServletRequest.getRemoteAddr();
			}
		}

		return null;
	}

}
