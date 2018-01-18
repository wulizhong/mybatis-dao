package org.mybatis.dao.condation;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGrenerator {

	private static  AtomicInteger id = new AtomicInteger(Integer.MIN_VALUE);
	
	public static int grenerateId(){
		
		if(id.get() == Integer.MAX_VALUE-100){
			id.set(Integer.MIN_VALUE);
		}
		return id.incrementAndGet();
	}
}
