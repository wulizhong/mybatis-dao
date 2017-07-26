package org.mybatis.dao;
/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 下午4:29:29 
 * 
 */
public interface ObjectCreator {

	public <T> T create(Class<T> clazz);
	
}
