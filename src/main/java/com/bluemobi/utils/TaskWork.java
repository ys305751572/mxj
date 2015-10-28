package com.bluemobi.utils;

import java.util.TimerTask;

/**
 * 定时任务抽象类，
 * 主要保证在任务执行完成后能够停止该任务
 * @author yesong
 *
 */
public abstract class TaskWork extends TimerTask{

	@Override
	public void run() {
		try {
			execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.cancel();
		}
	}
	public abstract void execute();
}
