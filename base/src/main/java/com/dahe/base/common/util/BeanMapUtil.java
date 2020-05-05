package com.dahe.base.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanMapUtil {

	public static Map<String, Object> bean2Map(Object bean) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Class<?> beanClass = bean.getClass();
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for(int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if("class" != propertyName.intern()) {
					Method readMethod = descriptor.getReadMethod();
					if(readMethod == null) {
						continue;
					}
					try {
						Object value = readMethod.invoke(bean, new Object[0]);
						returnMap.put(propertyName, value);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IntrospectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return returnMap;
	}
	
	/*public static void main(String[] args) {
		Human human = new Human();
		human.setAge(30);
		human.setSex("male");
		Map<String, Object> map = bean2Map(human);
		System.out.println(map.toString());
	}*/
}
