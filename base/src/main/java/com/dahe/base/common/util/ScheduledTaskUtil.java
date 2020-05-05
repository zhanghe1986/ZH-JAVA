package com.dahe.base.common.util;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dahe.base.common.entity.Tasks;

public class ScheduledTaskUtil {
	private static HashMap<String, ScheduledExecutorService> scheduledTaskMap = new HashMap<String, ScheduledExecutorService>();
	
	public ScheduledTaskUtil() {}
	
	public static void execute(final Tasks tasks) {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		scheduledTaskMap.put(tasks.getClass().toString(), service);
		System.out.println(scheduledTaskMap.toString());
		Runnable runnable = new Runnable() {
			public void run() {
				System.out.println(tasks.showTasksInfo());
			}
		};
		service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);
	}
}
