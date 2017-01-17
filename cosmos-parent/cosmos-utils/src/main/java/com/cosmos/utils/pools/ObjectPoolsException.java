package com.cosmos.utils.pools;

public class ObjectPoolsException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ObjectPoolsException(String msg){
		super(msg);
	}
	
	public ObjectPoolsException(String msg,Throwable throwable){
		super(msg,throwable);
	}
	
	public ObjectPoolsException(){
		
	}
}
