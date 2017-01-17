package com.cosmos.workflow.runtime.factory;

import com.cosmos.workflow.activities.Activity;

public abstract class WorkflowInstance<T extends Activity> {
	
	private String key;
	
	private long timeout;
	
	private long time;
	
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public abstract T getExecutableActivity(Object obj);
}
