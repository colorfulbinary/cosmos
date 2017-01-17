package com.cosmos.workflow.activities;



public class WorkflowException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private Activity activity;
	
	public WorkflowException(Activity activity){
		this.activity = activity;
	}
	
	public WorkflowException(Activity activity,String message){
		super(message);
		this.activity = activity;
	}
	
	public WorkflowException(Activity activity,String message,Throwable throwable){
		super(message,throwable);
		this.activity = activity;
	}


	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
}
