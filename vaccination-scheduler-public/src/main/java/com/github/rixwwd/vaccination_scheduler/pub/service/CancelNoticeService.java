package com.github.rixwwd.vaccination_scheduler.pub.service;

import com.github.rixwwd.vaccination_scheduler.pub.entity.Cell;
import com.github.rixwwd.vaccination_scheduler.pub.entity.PublicUser;

public interface CancelNoticeService {

	/**
	 * キャンセルが発生したことを通知する。
	 * 
	 * @param publicUser 通知先の {@link PublicUser}
	 * @param cell       キャンセルが発生した{@link Cell }
	 */
	void notifyCancel(Cell cell);

	/**
	 * キャンセル待ちに追加したことを通知する。
	 * 
	 * @param publicUser 通知先の {@link PublicUser}
	 * @param cell       キャンセル待ちの{@link Cell }
	 */
	void notifyAddWaitingList(PublicUser publicUser, Cell cell);
}
