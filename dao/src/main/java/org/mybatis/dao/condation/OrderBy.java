package org.mybatis.dao.condation;

import java.util.Map;

import org.mybatis.dao.Condation;
import org.mybatis.dao.TableMap;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月13日 下午4:50:13 
 * 
 */
public class OrderBy implements Condation{

	private String name;

	private String value;
	
	private Condation condation;
	
	public OrderBy(Condation condation,String name, String value){
		this.condation = condation;
		this.name = name;
		this.value = value;
	}
	
	public OrderBy(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toSql(Class<?> clazz,Map<String,Object> paramter) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		if(condation!=null){
			builder.append(condation.toSql(clazz,paramter));
			if(builder.indexOf("ORDER BY")==-1){
				builder.append(" ORDER BY ");
			}else{
				builder.append(" , ");
			}
		}else{
			builder.append(" ORDER BY ");
		}
		
		builder.append(TableMap.getInstance().getTableMap(clazz).getDataBaseField(name));
		builder.append(" ");
		builder.append(value);
		return builder.toString();
	}

	public OrderBy orderBy(String name,String value){
		OrderBy ob = new OrderBy(this,name,value);
		return ob;
	}
	
	public OrderBy orderBy(String name){
		OrderBy ob = new OrderBy(this,name,Cnd.ASC);
		condation = ob;
		return ob;
	}
}
