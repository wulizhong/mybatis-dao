package org.mybatis.dao;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import org.mybatis.dao.annotation.Column;
import org.mybatis.dao.annotation.Ignore;
import org.mybatis.dao.annotation.Seq;
import org.mybatis.dao.database.Id;
import org.mybatis.dao.database.Many;
import org.mybatis.dao.database.ManyToMany;
import org.mybatis.dao.database.One;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.util.StringUtils;
import org.mybatis.dao.util.Toolkit;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月13日 下午2:29:43
 * 
 */
public class TableMap {

	private ConcurrentHashMap<Class<?>, Table> tableMap = new ConcurrentHashMap<Class<?>, Table>();

	private TableMap() {

	}

	private static TableMap instance;

	private boolean underline = true;

	public static TableMap getInstance() {
		if (instance == null) {
			synchronized (TableMap.class) {
				instance = new TableMap();
			}
		}
		return instance;
	}

	public Table getTableMap(Class<?> clazz) {
		Table table = tableMap.get(clazz);
		if (table == null) {
			table = generateTableMap(clazz);
			tableMap.put(clazz, table);
		}
		return table;
	}

	protected Table generateTableMap(Class<?> clazz) {
		Table table = new Table();
		org.mybatis.dao.annotation.Table tableAnnotation = clazz.getAnnotation(org.mybatis.dao.annotation.Table.class);
		String tableName;
		if (tableAnnotation != null) {
			if (!StringUtils.isEmpty(tableAnnotation.value())) {
				tableName = tableAnnotation.value();
			} else {
				tableName = underline ? StringUtils.humpToUnderline(clazz.getSimpleName()) : clazz.getSimpleName();
			}
		} else {
			tableName = underline ? StringUtils.humpToUnderline(clazz.getSimpleName()) : clazz.getSimpleName();
		}
		table.setName(tableName);
		Field fields[] = clazz.getDeclaredFields();
		for (Field f : fields) {
			Ignore ignore = f.getAnnotation(Ignore.class);
			if (ignore != null) {
				continue;
			}
			
			org.mybatis.dao.annotation.Id id = f.getAnnotation(org.mybatis.dao.annotation.Id.class);
			
			if (id == null) {
				if ("id".equals(f.getName().toLowerCase())) {
					Seq seq = f.getAnnotation(Seq.class);
					Id recordId = new Id();
					recordId.setId("id");
					recordId.setField(f);
					recordId.setAutoGenerateId(f.getType() != String.class);
					if(seq!=null){
						recordId.setSequence(seq.value());
					}
					table.setId(recordId);

					table.putDataBaseField(f, "id");
					table.putField("id", f);
					table.putOriginalFieldMap("id", f);
					continue;
				}
			} else {
				String idStr = id.value();
				if (StringUtils.isEmpty(idStr)) {
					idStr = underline ? StringUtils.humpToUnderline(f.getName()) : f.getName();
				}
				Seq seq = f.getAnnotation(Seq.class);
				
				Id recordId = new Id();
				recordId.setId(idStr);
				recordId.setField(f);
				if(seq!=null){
					recordId.setSequence(seq.value());
				}
				recordId.setAutoGenerateId(id.autoGenerateId());
				table.setId(recordId);

				table.putDataBaseField(f, idStr);
				table.putField(idStr, f);
				table.putOriginalFieldMap(f.getName(), f);
				continue;
			}

			if (Toolkit.isBasicType(f.getType())) {
				Column column = f.getAnnotation(Column.class);
				String fieldName = f.getName();
				String columnName;
				if (column == null) {
					columnName = underline ? StringUtils.humpToUnderline(f.getName()) : f.getName();
				} else {
					columnName = column.value();
					if (StringUtils.isEmpty(columnName)) {
						columnName = underline ? StringUtils.humpToUnderline(f.getName()) : f.getName();
					}
				}
				table.putDataBaseField(f, columnName);
				table.putField(columnName, f);
				table.putOriginalFieldMap(fieldName, f);
			} else {

				if (Toolkit.isListType(f.getType())) {

					org.mybatis.dao.annotation.Many manyAnnotation = f.getAnnotation(org.mybatis.dao.annotation.Many.class);
					org.mybatis.dao.annotation.ManyToMany manyToManyAnnotation = f.getAnnotation(org.mybatis.dao.annotation.ManyToMany.class);

					if (manyAnnotation == null && manyToManyAnnotation == null) {
						continue;
					}

					Class<?> type = Toolkit.getGenericType(f);

					if (!Toolkit.isBasicType(type)) {
						if (manyAnnotation != null) {
							String manyFieldId = null;
							if (!StringUtils.isEmpty(manyAnnotation.value())) {
								manyFieldId = manyAnnotation.value();
							}
							if (manyFieldId == null) {
								continue;
							}
							Many many = new Many();
							many.setFetchType(manyAnnotation.fetchType());
							many.setField(f);
							many.setManyIdFieldName(manyFieldId);
							many.setType(type);
							if (many.getFetchType() == FetchType.LAZY)
								table.setNeedProxy(true);
							table.putManyMapField(f.getName(), many);

						} else if (manyToManyAnnotation != null) {
							
							ManyToMany manyToMany = new ManyToMany();
							manyToMany.setFetchType(manyToManyAnnotation.fetchType());
							manyToMany.setField(f);
							manyToMany.setRelationTableName(manyToManyAnnotation.relation());
							manyToMany.setFromId(manyToManyAnnotation.from());
							manyToMany.setToId(manyToManyAnnotation.to());
							manyToMany.setType(type);
							if (manyToMany.getFetchType() == FetchType.LAZY)
								table.setNeedProxy(true);
							
							table.putManyToManyMapField(f.getName(), manyToMany);
						}else{
							continue;
						}

					}

				} else {
					String oneFieldId = null;
					org.mybatis.dao.annotation.One oneAnnotation = f.getAnnotation(org.mybatis.dao.annotation.One.class);
					One one = new One();
					if (oneAnnotation != null) {
						if (!StringUtils.isEmpty(oneAnnotation.value())) {
							oneFieldId = oneAnnotation.value();
						}
						one.setFetchType(oneAnnotation.fetchType());

					} else {
						one.setFetchType(FetchType.LAZY);
					}
					if (oneFieldId == null)
						oneFieldId = f.getName() + "Id";

					one.setOneIdFieldName(oneFieldId);
					one.setField(f);
					if (one.getFetchType() == FetchType.LAZY)
						table.setNeedProxy(true);
					table.putOneMapField(f.getName(), one);
				}

			}
		}
		return table;
	}
}
