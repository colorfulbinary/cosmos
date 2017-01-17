package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.call.CreateObjectActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class CreateObjectActivityInitializer implements IXmlInitializer<CreateObjectActivity>{

	@Override
	public void init(CreateObjectActivity activity, Element item)
			throws WorkflowRuntimeException {
//		String in = item.attributeValue("in");
//		activity.setIn(in);
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指明一个输出!");
		}
		activity.setOut(out);
		String classPath = item.attributeValue("class");
		String initTypes = item.attributeValue("types");
		String initArguments = item.attributeValue("arguments");
		if(classPath == null || "".equals(classPath.trim())){
			throw new WorkflowRuntimeException("create-object:没有找到classpath!");
		}
		activity.setClassPath(classPath);
		if(initTypes == null || "".equals(initTypes.trim())){
			activity.setTypes(new String[0]);
		} else {
			activity.setTypes(initTypes.trim().split(","));
		}
		if(initArguments == null || "".equals(initArguments.trim())){
			activity.setArgumentNames(new String[0]);
		} else {
			activity.setArgumentNames(initArguments.trim().split(","));
		}
	}

}
