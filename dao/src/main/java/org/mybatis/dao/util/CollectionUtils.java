package org.mybatis.dao.util;

import java.util.Collection;

/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月17日 下午3:17:04 
 * 
 */
public class CollectionUtils {

	public static boolean isEmpty(Collection<?> collection){
		if(collection == null)
			return true;
		return collection.isEmpty();
	}
}
