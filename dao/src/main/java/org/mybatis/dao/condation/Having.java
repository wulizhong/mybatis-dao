package org.mybatis.dao.condation;

import java.util.Map;

import org.mybatis.dao.Condation;



/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午4:24:49 
 * 
 */
public class Having implements Condation{

	
	public Having(String name,String op,Object value){
		
	}
	
	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
