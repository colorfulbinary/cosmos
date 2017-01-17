package com.cosmos.utils.io.exception;

public class SignedTypeFormatException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public SignedTypeFormatException(){
		
	}
	
	public SignedTypeFormatException(String message){
		super(message);
	}
	
	
	public SignedTypeFormatException(String message,Throwable throwable){
		super(message,throwable);
	}
}
