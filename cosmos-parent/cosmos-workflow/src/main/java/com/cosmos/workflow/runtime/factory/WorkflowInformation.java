package com.cosmos.workflow.runtime.factory;

import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;


public interface WorkflowInformation<T extends Activity> {
	
	public WorkflowInstance<T> getItemById(String key) throws WorkflowRuntimeException;
	
	public WorkflowInstance<T>[] getItems(boolean isLoad) throws WorkflowRuntimeException; 
		
	public String getWorkflowId();
	
	public String getTag();
}
