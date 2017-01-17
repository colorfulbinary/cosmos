package com.cosmos.workflow.activities;

import java.io.Serializable;


public abstract class Activity implements Serializable{
	
	private static final long serialVersionUID = 0L;
	
	private String activityId;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	public void releaseResource(){
		this.activityId = null;
	}
	
	
}
