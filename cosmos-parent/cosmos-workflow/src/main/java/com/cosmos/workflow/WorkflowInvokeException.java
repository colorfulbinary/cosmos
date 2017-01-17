package com.cosmos.workflow;

public class WorkflowInvokeException extends Exception{

	private static final long serialVersionUID = 1L;

	public WorkflowInvokeException(String message){
		super(message);
	}
	
	public WorkflowInvokeException(String message,Throwable throwable){
		super(message,throwable);
	}
}
