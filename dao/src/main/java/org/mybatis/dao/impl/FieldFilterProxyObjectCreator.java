package org.mybatis.dao.impl;

import org.mybatis.dao.FieldFilter;
import org.mybatis.dao.ObjectCreator;
import org.mybatis.dao.TableMap;
import org.mybatis.dao.database.Table;
import org.mybatis.dao.selecte.SelectContext;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月19日 下午4:31:52
 * 
 */
public class FieldFilterProxyObjectCreator implements ObjectCreator {

	private SelectContext selectContext;
	private FieldFilter fieldFilter;
	public FieldFilterProxyObjectCreator(SelectContext selectContext,FieldFilter fieldFilter) {
		this.selectContext = selectContext;
		this.fieldFilter = fieldFilter;
	}

	@Override
	public <T> T create(Class<T> clazz) {
		// TODO Auto-generated method stub

		Table table = TableMap.getInstance().getTableMap(clazz);
		if (table.isNeedProxy()) {
			return new FieldFilterProxyObject(selectContext,fieldFilter).newProxyInstance(clazz);
		} else {
			try {
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

}
