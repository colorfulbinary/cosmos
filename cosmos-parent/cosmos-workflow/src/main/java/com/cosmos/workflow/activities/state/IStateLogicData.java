package com.cosmos.workflow.activities.state;

import com.cosmos.workflow.activities.WorkflowData;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public interface IStateLogicData extends WorkflowData<IStateLogicData>{
	
	public String getStateWorkflowId();
	
	public void setStateWorkflowId(String id);
	
	public void setSequenceLogicData(ISequenceLogicData data);
	
	public ISequenceLogicData toSequenceLogicData();
}
