package com.rouies.utils.ssh;

import java.io.IOException;

public class SSHException extends IOException{
	
	private static final long serialVersionUID = 1L;

	public SSHException(){}
	
	public SSHException(String message){
		super(message);
	}
	
	public SSHException(String message,Throwable throwable){
		super(message,throwable);
	}
}
