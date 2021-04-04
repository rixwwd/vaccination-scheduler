package com.github.rixwwd.vaccination_scheduler.pub.service;

import com.github.rixwwd.vaccination_scheduler.pub.entity.ActionType;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;

public interface ActionLogService {

	void log(PublicUser publicUser, ActionType actionType);
}
