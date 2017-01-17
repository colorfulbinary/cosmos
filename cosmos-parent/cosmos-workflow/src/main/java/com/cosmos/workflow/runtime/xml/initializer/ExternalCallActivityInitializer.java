package com.cosmos.workflow.runtime.xml.initializer;

import java.util.List;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.call.ExternalCallActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class ExternalCallActivityInitializer implements IXmlInitializer<ExternalCallActivity>{

	@Override
	public void init(ExternalCallActivity activity, Element item)
			throws WorkflowRuntimeException {
		String id = item.attributeValue("id");
		//类
		String in = item.attributeValue("in");
		activity.setIn(in);
		String out = item.attributeValue("out");
		activity.setOut(out);
		@SuppressWarnings("unchecked")
		List<Element> constructors = item.elements("constructor");
		if(constructors.size() != 1){
			throw new WorkflowRuntimeException(id + ":external-method没有找到constructor信息!");
		}
		Element constructor = constructors.get(0);
		//String scope = constructor.attributeValue("scope");
		String classPath = constructor.attributeValue("class");
		String initTypes = constructor.attributeValue("types");
		String initArguments = constructor.attributeValue("arguments");
		if(classPath == null || "".equals(classPath.trim())){
			throw new WorkflowRuntimeException(id + ":external-method:没有找到classpath!");
		}
		activity.setClassPath(classPath);
		//activity.setScope(scope == null ? "new" : scope);
		if(initTypes == null || "".equals(initTypes.trim())){
			activity.setInitTypes(new String[0]);
		} else {
			activity.setInitTypes(initTypes.trim().split(","));
		}
		
		if(initArguments == null || "".equals(initArguments.trim())){
			activity.setInitArgumentNames(new String[0]);
		} else {
			activity.setInitArgumentNames(initArguments.trim().split(","));
		}
		//方法
		@SuppressWarnings("unchecked")
		List<Element> methods = item.elements("method");
		if(methods.size() != 1){
			throw new WorkflowRuntimeException(id + ":external-method没有找到method信息!");
		}
		Element method = methods.get(0);
		String methodName = method.attributeValue("name");
		String types = method.attributeValue("types");
		String arguments = method.attributeValue("arguments");
		if(methodName == null || "".equals(methodName.trim())){
			throw new WorkflowRuntimeException(id + ":external-method:没有找到method name!");
		}
		activity.setMethodName(methodName);
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
