package org.mybatis.dao.impl;

import org.mybatis.dao.ObjectCreator;


/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 下午4:31:52 
 * 
 */
public class DefaultObjectCreator implements ObjectCreator {

	@Override
	public <T> T create(Class<T> clazz) {
		// TODO Auto-generated method stub
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
