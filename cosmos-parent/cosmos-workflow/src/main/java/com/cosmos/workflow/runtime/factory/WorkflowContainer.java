package com.cosmos.workflow.runtime.factory;

import java.util.Map;

import com.cosmos.workflow.activities.Activity;

public interface WorkflowContainer<T extends Activity> {
	public boolean clear() ;
	public void put(Map<String, WorkflowItem<T>> items);
	public void put(String key, WorkflowItem<T> item);
	public void remove(String key);
	public WorkflowItem<T> get(String key);
}
