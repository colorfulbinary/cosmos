package com.cosmos.utils.reflect;

public class ReflectException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ReflectException(){};
	
//	public ReflectException(String message){
//		super(message);
//	}
	
	public ReflectException(String message,Throwable throwable){
		super(message,throwable);
	}
}
