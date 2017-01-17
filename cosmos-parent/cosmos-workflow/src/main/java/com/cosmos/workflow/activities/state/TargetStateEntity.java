package com.cosmos.workflow.activities.state;

import com.cosmos.workflow.logic.Logic;

public class TargetStateEntity {
	private StateActivity targetActivity;
	
	
	private String targetExecutorId;
	
	public String getTargetExecutorId() {
		return targetExecutorId;
	}

	public void setTargetExecutorId(String targetExecutorId) {
		this.targetExecutorId = targetExecutorId;
	}

	private Logic targetLogic;

	public StateActivity getTargetActivity() {
		return targetActivity;
	}

	public void setTargetActivity(StateActivity targetActivity) {
		this.targetActivity = targetActivity;
	}

	public Logic getTargetLogic() {
		return targetLogic;
	}

	public void setTargetLogic(Logic targetLogic) {
		this.targetLogic = targetLogic;
	}
}
