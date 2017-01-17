package com.cosmos.workflow.activities.sequence.action.call;

import com.cosmos.utils.reflect.ClassReflectUtils;
import com.cosmos.utils.reflect.MemberReflectUtils;
import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class ExternalCallActivity extends ActionActivity{
	
	private static final long serialVersionUID = 2399970410259067540L;

	private String[] types;
	
	private String[] initTypes;
	
	private String classPath;
	
	private String methodName;
	
	private String[] argumentNames;
	
	private String[] initArgumentNames;

	public String[] getInitTypes() {
		return initTypes;
	}

	public void setInitTypes(String[] initTypes) {
		this.initTypes = initTypes;
	}

	public String[] getArgumentNames() {
		return argumentNames;
	}

	public void setArgumentNames(String[] argumentNames) {
		this.argumentNames = argumentNames;
	}

	public String[] getInitArgumentNames() {
		return initArgumentNames;
	}

	public void setInitArgumentNames(String[] initArgumentNames) {
		this.initArgumentNames = initArgumentNames;
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
		Object[] initArguments = data.getValueByNames(this.initArgumentNames);
		Object res;
		try {
			Object instance = ClassReflectUtils.getInstance(this.classPath, this.initTypes, initArguments);
			MemberReflectUtils<?> member = new MemberReflectUtils<Object>(instance);
			res = member.invoke(this.methodName, this.types, arguments);
		} catch (Exception e) {
			throw new WorkflowException(this,"调用外部方法错误:" + e.getMessage(),e);
		}
		if(!StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, res);
		}
	}

	@Override
	public void release() {
		this.types = null;
		this.argumentNames = null;
		this.initArgumentNames =null;
		this.initTypes = null;
		this.classPath = null;
		this.methodName = null;
	}
	
	public static void main(String[] args) {
		Object a  = null;
		System.out.println(a instanceof String);
	}
	
}
