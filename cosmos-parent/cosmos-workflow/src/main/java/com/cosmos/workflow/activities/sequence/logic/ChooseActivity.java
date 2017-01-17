package com.cosmos.workflow.activities.sequence.logic;

import java.util.ArrayList;
import java.util.List;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.SQActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class ChooseActivity extends LogicActivity{
	
	private static final long serialVersionUID = -1605300438650061741L;
	
	private boolean isOnly = true;
	
	public boolean isOnly() {
		return isOnly;
	}

	public void setOnly(boolean isOnly) {
		this.isOnly = isOnly;
	}

	private List<WhenActivity>  activities = new ArrayList<WhenActivity>();

	@Override
	public void appendChild(SQActivity act) {
		if(act instanceof WhenActivity){
			activities.add((WhenActivity)act);
		}
	}

	@Override
	public void execute(ISequenceLogicData data) throws WorkflowException {
		WhenActivity other = null;
		boolean isSuccess = false;
		for (WhenActivity act : activities) {
			if(act.getType() == WhenActivity.OTHER){
				other = act;
			} else {
				if(act.check(data)){
					isSuccess = true;
					act.execute(data);
					if(this.isOnly){
						break;
					}
				}
			}
		}
		if(other!=null && !isSuccess){
			other.execute(data);
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
