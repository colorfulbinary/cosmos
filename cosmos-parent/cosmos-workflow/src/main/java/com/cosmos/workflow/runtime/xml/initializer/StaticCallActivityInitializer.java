package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.call.StaticCallActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class StaticCallActivityInitializer implements IXmlInitializer<StaticCallActivity>{

	@Override
	public void init(StaticCallActivity activity, Element item)
			throws WorkflowRuntimeException {
		String id = item.attributeValue("id");
		//类
		String in = item.attributeValue("in");
		activity.setIn(in);
		String out = item.attributeValue("out");
		activity.setOut(out);
		String classPath = item.attributeValue("class");
		String types = item.attributeValue("types");
		String arguments = item.attributeValue("arguments");
		String methodName = item.attributeValue("name");
		if(methodName == null || "".equals(methodName.trim())){
			throw new WorkflowRuntimeException(id + ":static-method:没有找到method name!");
		}
		activity.setMethodName(methodName);
		if(classPath == null || "".equals(classPath.trim())){
			throw new WorkflowRuntimeException(id + ":static-method:没有找到classpath!");
		}
		activity.setClassPath(classPath);

		if(types == null || "".equals(types.trim())){
			activity.setTypes(new String[0]);
		} else {
			activity.setTypes(types.trim().split(","));
		}
		
		if(arguments == null || "".equals(arguments.trim())){
			activity.setArgumentNames(new String[0]);
		} else {
			activity.setArgumentNames(arguments.trim().split(","));
		}
	}

}
