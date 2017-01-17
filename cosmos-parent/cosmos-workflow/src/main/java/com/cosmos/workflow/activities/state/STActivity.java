package com.cosmos.workflow.activities.state;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.WorkflowInitializer;
import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.activities.IClean;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowFactory;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;

public abstract class STActivity extends Activity implements IClean{
	
	private static final long serialVersionUID = -8092955453992452815L;
	
	private String initializer;

	public void setInitializer(String initializer) {
		this.initializer = initializer;
	}
	
	public void initialize(ISequenceLogicData data) throws WorkflowException {
		if(!StringUtils.isEmptyOrNull(this.initializer)){
			WorkflowInstance<?> instance;
			try {
				WorkflowFactory<?> factory = WorkflowFactory.getWorkflowFactory(WorkflowInitializer.getSequenceFactoryPath());
				instance = factory.getWorkflowInstance(this.initializer);
			} catch (WorkflowRuntimeException e) {
				throw new WorkflowException(this,"创建实例sequence失败:" + this.initializer + e.getMessage(),e);
			}
			Activity act = instance.getExecutableActivity(null);
			if(act == null || !(act instanceof SequenceActivity)){
				throw new WorkflowException(this,"没有找到要执行的实例");
			}
			((SequenceActivity)act).execute(data);
		}
	}
	
	public abstract StateActivity execute(IStateLogicData data) throws WorkflowException;
	
	@Override
	public void releaseResource() {
		super.releaseResource();
		this.initializer = null;
		this.release();
	}
}
