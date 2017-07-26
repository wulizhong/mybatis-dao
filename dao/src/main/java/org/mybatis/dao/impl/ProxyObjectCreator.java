package org.mybatis.dao.impl;

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
public class ProxyObjectCreator implements ObjectCreator {

	private SelectContext selectContext;

	public ProxyObjectCreator(SelectContext selectContext) {
		this.selectContext = selectContext;
	}

	@Override
	public <T> T create(Class<T> clazz) {
		// TODO Auto-generated method stub

		Table table = TableMap.getInstance().getTableMap(clazz);
		if (table.isNeedProxy()) {

			return new ProxyObject(selectContext).newProxyInstance(clazz);

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
