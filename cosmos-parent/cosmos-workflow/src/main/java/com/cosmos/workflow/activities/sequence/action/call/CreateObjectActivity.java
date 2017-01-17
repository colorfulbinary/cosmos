package com.cosmos.workflow.activities.sequence.action.call;

import com.cosmos.utils.reflect.ClassReflectUtils;
import com.cosmos.utils.reflect.ReflectException;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class CreateObjectActivity extends ActionActivity{
	
	private static final long serialVersionUID = -768681171048242764L;

	private String classPath;
	
	private String[] types;
	
	private String[] argumentNames;
	
	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String[] getArgumentNames() {
		return argumentNames;
	}

	public void setArgumentNames(String[] argumentNames) {
		this.argumentNames = argumentNames;
	}

	@Override
	public void release() {
		this.classPath = null;
		this.types = null;
		this.argumentNames = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object[] args = data.getValueByNames(this.argumentNames);
		Object result;
		try {
			result = ClassReflectUtils.getInstance(this.classPath, this.types, args);
		} catch (ReflectException e) {
			throw new WorkflowException(this,"创建实例失败:" + e.getMessage(),e);
		}
		data.put(this.out, result);
	}

}
