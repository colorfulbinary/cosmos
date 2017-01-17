package com.cosmos.workflow.activities.sequence.logic;

import java.util.ArrayList;
import java.util.List;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.SQActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class SequenceActivity extends LogicActivity{
	
	private static final long serialVersionUID = 3796793294175657728L;
	
	private long timeout;
	
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	private List<SQActivity> activities =  new ArrayList<SQActivity>();

	@Override
	public void appendChild(SQActivity act) {
		activities.add(act);
	}

	@Override
	public void execute(ISequenceLogicData data) throws WorkflowException {
		for (int i = 0,len=activities.size(); i < len; i++) {
			try {
				activities.get(i).execute(data);
			} catch (WorkflowException e) {
				if(!this.isExceptionOnContinue()){
					throw e;
				}
			}
		}
	}

	@Override
	public void release() {
		for (SQActivity sqActivity : activities) {
			sqActivity.releaseResource();
		}
		this.activities.clear();
		this.activities = null;
	}
	
}
