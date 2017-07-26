package org.mybatis.dao;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月19日 下午5:10:25 
 * 
 */
public class FieldFilter {

	private FieldFilter(){
		includeFields = new ArrayList<String>();
		excludeFields = new ArrayList<String>();
	}
	
	private ArrayList<String> includeFields;
	
	private ArrayList<String> excludeFields;
	
	public static FieldFilter include(String ... fields){
		FieldFilter filter = new FieldFilter();
		for(String field : fields){
			filter.includeFields.add(field);
		}
		return filter;
	}
	
	public static FieldFilter exclude(String ... fields){
		FieldFilter filter = new FieldFilter();
		for(String field : fields){
			filter.excludeFields.add(field);
		}
		return filter;
	}

	public List<String> getIncludeFields() {
		return includeFields;
	}

	public List<String> getExcludeFields() {
		return excludeFields;
	}
	
}
