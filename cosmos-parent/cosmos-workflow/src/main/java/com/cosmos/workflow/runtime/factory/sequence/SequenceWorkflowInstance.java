package com.cosmos.workflow.runtime.factory.sequence;

import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;

public class SequenceWorkflowInstance extends WorkflowInstance<SequenceActivity>{

	private SequenceActivity root = null;
	
	protected void setRoot(SequenceActivity root) {
		this.root = root;
	}
	
	@Override
	public SequenceActivity getExecutableActivity(Object obj) {
		return this.root;
	}
}
