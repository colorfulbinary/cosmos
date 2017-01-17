package com.cosmos.utils.io.exception;

public class UnsignedTypeFormatException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public UnsignedTypeFormatException(){
		
	}
	
	public UnsignedTypeFormatException(String message){
		super(message);
	}
	
	public UnsignedTypeFormatException(String message,Throwable throwable){
		super(message,throwable);
	}

}
