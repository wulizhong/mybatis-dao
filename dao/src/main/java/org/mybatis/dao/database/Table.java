package org.mybatis.dao.database;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月13日 上午11:06:02
 * 
 */
public class Table {

	private String name;
	
	private Id id;
	
	private boolean needProxy;
	
	

	public boolean isNeedProxy() {
		return needProxy;
	}

	public void setNeedProxy(boolean needProxy) {
		this.needProxy = needProxy;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Table(String name){
		this.name = name;
	}
	
	public Table(){
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Map<Field, String> dataBaseFieldMap = new HashMap<>();
	
	private Map<String, Field> originalFieldMap = new HashMap<>();
	

	private Map<String, Field> fieldMap = new HashMap<>();
	
	private Map<String, One> oneMap = new HashMap<>();
	
	private Map<String, Many> manyMap = new HashMap<>();
	
	private Map<String, ManyToMany> manyToManyMap = new HashMap<>();
	
	public synchronized void putManyToManyMapField(String key, ManyToMany value) {
		manyToManyMap.put(key, value);
	}

	public ManyToMany getManyToManyMapField(String key){
		return manyToManyMap.get(key);
	}
	
	public Map<String, ManyToMany> getManyToManyMap() {
		return manyToManyMap;
	}

	public synchronized void putManyMapField(String key, Many value) {
		manyMap.put(key, value);
	}

	public Many getManyMapField(String key){
		return manyMap.get(key);
	}

	public Map<String, Many> getManyMap() {
		return manyMap;
	}


	public synchronized void putOneMapField(String key, One value) {
		oneMap.put(key, value);
	}

	public One getOneMapField(String key){
		return oneMap.get(key);
	}
	
	public Map<String, One> getOneMap() {
		return oneMap;
	}

	public Map<Field, String> getDataBaseFieldMap() {
		return dataBaseFieldMap;
	}

	public Map<String, Field> getOriginalFieldMap() {
		return originalFieldMap;
	}

	public Map<String, Field> getFieldMap() {
		return fieldMap;
	}

	public String getDataBaseField(Field field) {
		return dataBaseFieldMap.get(field);
	}

	public synchronized void putDataBaseField(Field key, String value) {
		dataBaseFieldMap.put(key, value);
	}
	
//	public String[] getDataBaseFields() {
//		return (String[]) dataBaseFieldMap.values().toArray(new String[]{});
//	}

	public Field getField(String key) {
		return fieldMap.get(key);
	}

	public synchronized void putField(String key, Field field) {
		fieldMap.put(key, field);
	}
	
	public synchronized void putOriginalFieldMap(String key, Field field) {
		originalFieldMap.put(key, field);
	}
	public Field getFieldByOriginalFieldName(String key) {
		return originalFieldMap.get(key);
	}

	public Field[] getFields() {
		return (Field[]) fieldMap.values().toArray();
	}
	/**
	 * 获取数据库对应的字段
	 * String
	 * @param fieldStr 可以是类的字段或者数据库的字段
	 * @return
	 */
	public String getDataBaseField(String fieldStr){
		Field field = getFieldByOriginalFieldName(fieldStr);
		if(field == null)
			return fieldStr;
		return getDataBaseField(field);
	}
	
}
