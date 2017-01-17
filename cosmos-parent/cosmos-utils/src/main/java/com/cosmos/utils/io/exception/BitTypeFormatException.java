package com.cosmos.utils.io.exception;

public class BitTypeFormatException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BitTypeFormatException(){
		
	}
	
	public BitTypeFormatException(String message){
		super(message);
	}
	
	public BitTypeFormatException(String message,Throwable throwable){
		super(message,throwable);
	}

}
