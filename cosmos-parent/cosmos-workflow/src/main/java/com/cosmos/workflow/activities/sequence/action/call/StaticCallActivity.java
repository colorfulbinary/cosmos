package com.cosmos.workflow.activities.sequence.action.call;

import com.cosmos.utils.reflect.MemberReflectUtils;
import com.cosmos.utils.reflect.ReflectException;
import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class StaticCallActivity extends ActionActivity{

	private static final long serialVersionUID = 1807258521999670546L;

	private String[] types;
	
	private String classPath;
	
	private String methodName;
	
	private String[] argumentNames;

	public String[] getArgumentNames() {
		return argumentNames;
	}

	public void setArgumentNames(String[] argumentNames) {
		this.argumentNames = argumentNames;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object[] arguments = data.getValueByNames(this.argumentNames);
		Object res;
		try {
			res = MemberReflectUtils.invoke(classPath, this.methodName, this.types, arguments);
		} catch (ReflectException e) {
			throw new WorkflowException(this,"调用静态方法错误:" + e.getMessage(),e);
		}
		if(!StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, res);
		}
	}

	@Override
	public void release() {
		this.types = null;
		this.argumentNames = null;
		this.classPath = null;
		this.methodName = null;
	}

}
