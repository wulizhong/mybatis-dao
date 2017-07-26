package org.mybatis.dao.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.dao.ObjectCreator;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Table;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月17日 下午4:48:57
 * 
 */
public class Toolkit {
	
	/** The CGLIB class separator: "$$" */
	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	public static boolean isBasicType(Class<?> t) {

		return t == String.class || t == Long.class || t == long.class || t == Integer.class || t == int.class || t == Short.class || t == short.class
				|| t == Byte.class || t == byte.class || t == char.class || t == StringBuilder.class || t == StringBuffer.class || t == BigInteger.class
				|| t == BigDecimal.class || t == Date.class || t == Double.class || t == double.class || t == Float.class || t == float.class;

	}

	public static boolean isListType(Class<?> t) {
		if (t == List.class) {
			return true;
		}
		Class<?>[] types = t.getInterfaces();
		if (types != null) {
			for (Class<?> type : types) {
				boolean isList = isListType(type);
				if (isList) {
					return true;
				}
			}
		}
		return false;
	}

	public static Class<?> getGenericType(Field f) {
		Type genericType = f.getGenericType();
		if (genericType == null)
			return null;
		if (genericType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericType;
			Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
			return genericClazz;
		}
		return null;
	}

	public static <T> T convertMapToObject(Class<T> clazz, Map<String, Object> map, ObjectCreator creator) {

		Table table = TableMap.getInstance().getTableMap(clazz);
		T obj = creator.create(clazz);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Field field = table.getField(key);
			if (field == null) {
				continue;
			}

			field.setAccessible(true);
			try {
				field.set(obj, entry.getValue());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			field.setAccessible(false);
		}
		return obj;
	}

	public static Map<String, Object> convertObjectToMap(Object obj) {
		if (obj == null)
			return null;
		HashMap<String, Object> map = new HashMap<>();
		Table table;
		if (isCglibProxy(obj)) {
			table = TableMap.getInstance().getTableMap(obj.getClass().getSuperclass());
		} else {
			table = TableMap.getInstance().getTableMap(obj.getClass());
		}
		try {
			for (Map.Entry<String, Field> entry : table.getOriginalFieldMap().entrySet()) {
				Field field = entry.getValue();
				field.setAccessible(true);
				map.put(entry.getKey(), field.get(obj));
				field.setAccessible(false);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static <T> List<T> convertMapToObjectList(Class<T> clazz, List<Map<String, Object>> mapList, ObjectCreator creator) {
		ArrayList<T> objList = new ArrayList<T>();

		for (Map<String, Object> map : mapList) {
			objList.add(convertMapToObject(clazz, map, creator));
		}

		return objList;
	}

	/**
	 * Check whether the given object is a CGLIB proxy.
	 * 
	 * @param object
	 *            the object to check
	 * @see org.springframework.aop.support.AopUtils#isCglibProxy(Object)
	 */
	public static boolean isCglibProxy(Object object) {
		return isCglibProxyClass(object.getClass());
	}

	/**
	 * Check whether the specified class is a CGLIB-generated class.
	 * 
	 * @param clazz
	 *            the class to check
	 */
	public static boolean isCglibProxyClass(Class<?> clazz) {
		return (clazz != null && isCglibProxyClassName(clazz.getName()));
	}

	/**
	 * Check whether the specified class name is a CGLIB-generated class.
	 * 
	 * @param className
	 *            the class name to check
	 */
	public static boolean isCglibProxyClassName(String className) {
		return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));
	}
}
