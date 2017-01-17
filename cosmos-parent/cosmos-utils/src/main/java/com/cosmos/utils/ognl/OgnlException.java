package com.cosmos.utils.ognl;

/*
 * Name      : OgnlException
 * Creator   : rouies
 * Function  : OGNL表达式执行时发生异常的类型
 * Date      : 2016-1-18
 */
public class OgnlException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public OgnlException(){
		
	}
	
	public OgnlException(String message){
		super(message);
	}
	
	public OgnlException(String message,Throwable throwable){
		super(message,throwable);
	}

}
