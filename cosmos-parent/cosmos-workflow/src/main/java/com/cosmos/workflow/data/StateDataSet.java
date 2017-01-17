package com.cosmos.workflow.data;

import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.cosmos.workflow.activities.state.IStateLogicData;

public class StateDataSet implements IStateLogicData{

	private String stateWorkflowId;
	
	private ISequenceLogicData sdata;
	
	@Override
	public ISequenceLogicData toSequenceLogicData() {
		return this.sdata;
	}

	@Override
	public String getStateWorkflowId() {
		return this.stateWorkflowId;
	}

	@Override
	public void setStateWorkflowId(String id) {
		this.stateWorkflowId = id;
	}


	@Override
	public void setSequenceLogicData(ISequenceLogicData data) {
		this.sdata = data;
	}

}
