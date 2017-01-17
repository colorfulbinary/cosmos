package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.call.OgnlGetterActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class OgnlGetterActivityInitializer implements IXmlInitializer<OgnlGetterActivity>{

	@Override
	public void init(OgnlGetterActivity activity, Element item)
			throws WorkflowRuntimeException {
		String in = item.attributeValue("in");
		activity.setIn(in);
		String out = item.attributeValue("out");
		activity.setOut(out);
		String ognlString = item.attributeValue("ognl");
		String args = item.attributeValue("arguments");
		if(ognlString == null || ognlString.trim().equals("")){
			throw new WorkflowRuntimeException("必须指定一个ognl表达式");
		}
		activity.setOgnlString(ognlString);
		if(args == null || args.trim().equals("")){
			activity.setArgs(new String[0]);
		} else {
			activity.setArgs(args.split(","));
		}
	}

}
