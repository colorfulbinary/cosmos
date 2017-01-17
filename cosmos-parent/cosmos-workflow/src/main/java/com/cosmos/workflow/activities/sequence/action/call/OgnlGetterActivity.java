package com.cosmos.workflow.activities.sequence.action.call;

import com.cosmos.utils.ognl.OgnlContext;
import com.cosmos.utils.ognl.OgnlException;
import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class OgnlGetterActivity extends ActionActivity{
	
	private static final long serialVersionUID = 3663343110441895786L;

	private String ognlString;
	
	private String[] args;
	

	public String getOgnlString() {
		return ognlString;
	}

	public void setOgnlString(String ognlString) {
		this.ognlString = ognlString;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public void release() {
		ognlString = null;
		args = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object[] argsObject = data.getValueByNames(args);
		Object result;
		try {
			result = OgnlContext.execute(data, this.ognlString, argsObject);
		} catch (OgnlException e) {
			throw new WorkflowException(this, "Ognl执行错误:" + e.getMessage(),e);
		}
		if(StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, result);
		}
	}

}
