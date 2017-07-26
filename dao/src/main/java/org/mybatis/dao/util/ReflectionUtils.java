package org.mybatis.dao.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

	public static Object getValue(Object obj,Field field){
		Object result = null;
		field.setAccessible(true);
		try {
			result = field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		field.setAccessible(false);
		return result;
	}
	
	public static void setValue(Object obj,Field field,Object value){
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		field.setAccessible(false);
	}
}
