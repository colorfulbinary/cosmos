package com.cosmos.workflow.activities.sequence.action;

import com.cosmos.workflow.activities.IClean;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.SQActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.cosmos.workflow.activities.sequence.logic.CompensateActivity;
import com.cosmos.workflow.activities.sequence.logic.FinalActivity;

public abstract class ActionActivity extends SQActivity implements IClean{
	
	private static final long serialVersionUID = 5638661689634210969L;

	protected String in;
	
	protected String out;
	
	public String getIn() {
		return in;
	}

	public void setIn(String in) {
		this.in = in;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	private CompensateActivity compensation;
	
	private FinalActivity a_final;
	
	public CompensateActivity getCompensation() {
		return compensation;
	}

	public void setCompensation(CompensateActivity compensation) {
		this.compensation = compensation;
	}
	
	public FinalActivity getFinal() {
		return a_final;
	}

	public void setFinal(FinalActivity a_final) {
		this.a_final = a_final;
	}
	
	public boolean compensate(ISequenceLogicData data,Exception e){
		SQActivity compensation = this.getCompensation();
		data.setException(e);
		boolean isCompensated = false;
		if(compensation != null){
			try {
				compensation.execute(data);
				isCompensated = true;
			} catch (Exception ex) {
				isCompensated = false;
			}
		}
		return isCompensated;
	}
	
	public void executeFinalActivity(ISequenceLogicData data){
		SQActivity afinal = this.getFinal();
		if(afinal != null){
			try {
				afinal.execute(data);
			} catch (Exception ex) {
				
			}
		}
	}
	
	@Override
	public void execute(ISequenceLogicData data) throws WorkflowException {
		try {
			this.action(data);
		} catch (WorkflowException e) {
			this.compensate(data, e);
			throw e;
		} finally {
			this.executeFinalActivity(data);
		}
	}
	
	public abstract void action(ISequenceLogicData data) throws WorkflowException;
	
	@Override
	public void releaseResource() {
		super.releaseResource();
		this.compensation = null;
		this.release();
	}
}
