package com.cosmos.utils.converter;

public class ConvertException extends Exception{
	private static final long serialVersionUID = 1L;
	public ConvertException(){
	}
	
	public ConvertException(String msg){
		super(msg);
	}
	
	public ConvertException(String msg,Throwable throwable){
		super(msg,throwable);
	}

}
