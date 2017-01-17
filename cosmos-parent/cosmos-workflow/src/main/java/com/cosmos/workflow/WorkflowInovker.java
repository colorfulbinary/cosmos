package com.cosmos.workflow;

import java.util.HashMap;
import java.util.Map;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.activities.state.StateActivity;
import com.cosmos.workflow.data.SequenceDataSet;
import com.cosmos.workflow.data.StateDataSet;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowFactory;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;

public class WorkflowInovker {
	
	public static void invoke(String sequenceId,Map<Object, Object> data) throws WorkflowInvokeException{
		try {
			WorkflowFactory<SequenceActivity> factory = WorkflowFactory.getWorkflowFactory(WorkflowInitializer.getSequenceFactoryPath(),SequenceActivity.class);
			WorkflowInstance<SequenceActivity> instance = factory.getWorkflowInstance(sequenceId);
			if(data == null){
				data = new HashMap<Object, Object>();
			}
			instance.getExecutableActivity(null).execute(new SequenceDataSet(data));
		} catch (Exception e) {
			throw new WorkflowInvokeException(e.getMessage(),e);
		} 
	}
	
	public static StateActivity invoke(String workflowId,String stateId,Map<Object, Object> data) throws WorkflowInvokeException{
		StateActivity result = null;
		StateDataSet ds = new StateDataSet();
		ds.setStateWorkflowId(workflowId);
		if(data == null){
			data = new HashMap<Object, Object>();
		}
		ds.setSequenceLogicData(new SequenceDataSet(data));
		WorkflowInstance<StateActivity> ins;
		try {
			WorkflowFactory<StateActivity> factory = WorkflowFactory.getWorkflowFactory(WorkflowInitializer.getStateFactoryPath(),StateActivity.class);
			ins = factory.getWorkflowInstance("test_demo");
		} catch (WorkflowRuntimeException e) {
			throw new WorkflowInvokeException(e.getMessage(),e);
		}
		StateActivity state = ins.getExecutableActivity(stateId);
		if(state == null){
			throw new WorkflowInvokeException("没有找到指定的状态活动");
		}
		try {
			result = state.execute(ds);
		} catch (WorkflowException e) {
			throw new WorkflowInvokeException(e.getMessage(),e);
		}
		return result;
	}
}
