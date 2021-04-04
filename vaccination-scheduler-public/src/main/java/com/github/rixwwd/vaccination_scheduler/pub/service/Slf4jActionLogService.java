package com.github.rixwwd.vaccination_scheduler.pub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.rixwwd.vaccination_scheduler.pub.entity.ActionLog;
import com.github.rixwwd.vaccination_scheduler.pub.entity.ActionType;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;

public class Slf4jActionLogService implements ActionLogService {

	private final Logger logger = LoggerFactory.getLogger(Slf4jActionLogService.class);

	@Override
	public void log(PublicUser publicUser, ActionType actionType) {
		var actionLog = new ActionLog();
		actionLog.setPublicUserId(publicUser.getId());
		actionLog.setActionType(actionType);
		actionLog.setIpAddress(getRemoteAddress());
		logger.info(actionLog.toString());
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
