package org.mybatis.dao.exception;
/** 
 * @author 作者 :吴立中
 * @version 1.0
 * @date	创建时间：2016年7月24日 下午2:35:25 
 * 
 */
public class IdIsNullException extends RuntimeException{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -2170578593426916680L;

    public IdIsNullException() {
        super();
    }

    public IdIsNullException(String s) {
        super(s);
    }
}
