package com.dahe.base.common.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tasks {
	private Process process;
	private StringBuilder tasksInfo;
	
	public Tasks() {}
	
	public String showTasksInfo() {
		BufferedReader bufferedReader = null;
		try {
			process = Runtime.getRuntime().exec("tasklist");
			bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream(), "UTF-8"));
			String line;
			tasksInfo = new StringBuilder();
			while((line = bufferedReader.readLine()) != null) {
				tasksInfo.append(line).append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(process != null) {
				try {
					process.getInputStream().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				process.destroy();
			}
		}
		return tasksInfo.toString();
	}
}
