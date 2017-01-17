package com.cosmos.workflow.activities.sequence.data;

import java.util.Map;

import com.cosmos.workflow.activities.WorkflowData;

public interface ISequenceLogicData extends WorkflowData<ISequenceLogicData>,ILogicData,Map<Object, Object>{
	
	public Exception getException();
	
	public <T> T getValue(Object key,Class<T> clazz) throws Exception;

	public void setException(Exception exception);
	
	public int getIndex();
	
	public void setIndex(int index);
	
	public boolean isSuccess();
	
	public void setBreak(boolean isbreak);
	
	public boolean isBreak();
	
	public void setContinue(boolean isContinue);
	
	public boolean isContinue();

	public void setSuccess(boolean isSuccess);
	
	public String getBoundValue(String name);

}
