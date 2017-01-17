package com.cosmos.workflow.activities.sequence;

import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.cosmos.workflow.logic.Logic;

public abstract class SQActivity extends Activity{
	
	private static final long serialVersionUID = -3941030196581928581L;
	
	private Logic logic;
	
	protected Logic getLogic() {
		return logic;
	}

	protected void setLogic(Logic logic) {
		this.logic = logic;
	}
	
	@Override
	public void releaseResource() {
		super.releaseResource();
		this.logic = null;
	}
	
	public abstract void execute(ISequenceLogicData data) throws WorkflowException;
}
