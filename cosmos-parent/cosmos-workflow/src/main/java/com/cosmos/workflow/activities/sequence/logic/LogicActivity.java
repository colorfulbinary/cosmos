package com.cosmos.workflow.activities.sequence.logic;

import com.cosmos.workflow.activities.IClean;
import com.cosmos.workflow.activities.sequence.SQActivity;

public abstract class LogicActivity extends SQActivity  implements IClean{
	
	private static final long serialVersionUID = -4949604993241336889L;
	
	private boolean exceptionOnContinue = false;

	public boolean isExceptionOnContinue() {
		return exceptionOnContinue;
	}

	public void setExceptionOnContinue(boolean exceptionOnContinue) {
		this.exceptionOnContinue = exceptionOnContinue;
	}

	public abstract void appendChild(SQActivity act);
	
	@Override
	public void releaseResource() {
		super.releaseResource();
		this.release();
	}

}
