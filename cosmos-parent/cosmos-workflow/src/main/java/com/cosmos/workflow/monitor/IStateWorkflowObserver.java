package com.cosmos.workflow.monitor;

public interface IStateWorkflowObserver {
	
	public boolean isOpen();
	
	public String getTaskId();
	
	public String getWorkflowId();
	
	public String getCurrentStateId();
	
	public void updata(String currentStateId);
	
	public void clear();
}
