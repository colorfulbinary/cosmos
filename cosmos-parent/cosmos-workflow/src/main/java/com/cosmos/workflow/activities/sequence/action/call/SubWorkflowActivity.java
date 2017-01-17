package com.cosmos.workflow.activities.sequence.action.call;

import com.cosmos.workflow.WorkflowInitializer;
import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowFactory;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;

public class SubWorkflowActivity extends ActionActivity{
	
	private static final long serialVersionUID = -2435204342519682741L;
	
	private String ref = null;

	@Override
	public void release() {
		this.ref = null;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		WorkflowInstance<?> instance;
		try {
			WorkflowFactory<?> factory = WorkflowFactory.getWorkflowFactory(WorkflowInitializer.getSequenceFactoryPath());
			instance = factory.getWorkflowInstance(this.ref);
		} catch (WorkflowRuntimeException e) {
			throw new WorkflowException(this,"子流程初始化错误:" + e.getMessage(),e);
		}
		Activity act = instance.getExecutableActivity(null);
		if(act == null || !(act instanceof SequenceActivity)){
			throw new WorkflowException(this,"子流程初始化错误:工厂类型不匹配");
		}
		((SequenceActivity)act).execute(data);
	}

}
