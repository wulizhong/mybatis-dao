package org.mybatis.dao;

import java.util.Map;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午4:10:51 
 * 
 */
public interface Condation {
	public String toSql(Class<?> clazz,Map<String,Object> paramter);
}
