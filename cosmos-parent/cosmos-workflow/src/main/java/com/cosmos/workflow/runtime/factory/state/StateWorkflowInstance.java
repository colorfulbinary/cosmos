package com.cosmos.workflow.runtime.factory.state;

import java.util.Iterator;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.state.StateActivity;
import com.cosmos.workflow.activities.state.TargetStateEntity;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;

public class StateWorkflowInstance extends WorkflowInstance<StateActivity>{
	
	private StateActivity root = null;
	
	protected void setRoot(StateActivity root) {
		this.root = root;
	}

	@Override
	public StateActivity getExecutableActivity(Object obj) {
		StateActivity result = null;
		if(obj == null){
			result = this.root;
		} else if(!StringUtils.isEmptyOrNull(obj)){
			result = this.getStateById(obj.toString(),this.root);
		}
		return result;
	}
	
	private StateActivity getStateById(String id,StateActivity act){
		StateActivity result = null;
		if(id.trim().equals(act.getActivityId().trim())){
			result = act;
		} else {
			Iterator<TargetStateEntity> iterator = act.targetIterator();
			while (iterator.hasNext()) {
				TargetStateEntity next = iterator.next();
				StateActivity targetActivity = next.getTargetActivity();
				result = this.getStateById(id, targetActivity);
				if(result != null){
					break;
				}
			}
		}
		return result;
	}
	
	

}
