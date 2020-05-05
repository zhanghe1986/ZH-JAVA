package com.dahe.base.common.util;

import java.lang.reflect.Method;

public class ObjectReflectProxyUtil {

	/**
	 * ObjectPath "memory.main.com.dahe.manager.DataManager"
	 * */
	public static void methodExecute(String ObjectPath) {
		Class<?> clazz;
		try {
			clazz = Class.forName(ObjectPath);
			Object obj = clazz.newInstance();
			Method method = clazz.getMethod("isThirdStore", new Class<?>[]{});
			System.out.println(method.invoke(obj, new Object[]{}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
