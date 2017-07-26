package org.mybatis.dao.condation;


/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午5:11:10 
 * 
 */
public class Cnd {

	public final static String EQ = "=";
	
	public final static String LIKE = "like";
	
	public static Where where(String name,String op,Object value){
		return new Where(name, op, value);
	}
}
