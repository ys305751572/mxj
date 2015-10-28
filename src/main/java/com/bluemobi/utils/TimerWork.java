package com.bluemobi.utils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时任务
 * @author yesong
 *
 */
public class TimerWork {

	private static Timer timer;
	
	@SuppressWarnings("deprecation")
	public static void addTask(TimerTask task,String date) {
		System.out.println(timer == null);
		if(timer == null) {
			reloadTimer();
		}
		Date date1 = new Date(date);
		long delay = date1.getTime();
		timer.schedule(task, delay);
	}

	private static void reloadTimer() {
		load();
	}

	private static void load() {
		timer = new Timer();
	}
}
