package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.convertor.TypeConvertActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class TypeConvertActivityInitializer implements IXmlInitializer<TypeConvertActivity>{

	@Override
	public void init(TypeConvertActivity activity, Element item)
			throws WorkflowRuntimeException {
		String in = item.attributeValue("in");
		if(StringUtils.isEmptyOrNull(in)){
			throw new WorkflowRuntimeException("必须指定一个要转换的对象!");
		}
		activity.setIn(in);
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指定一个输出位置!");
		}
		activity.setOut(out);
		String ognlString = item.attributeValue("to");
		String args = item.attributeValue("init-value");
		if(ognlString == null || ognlString.trim().equals("")){
			throw new WorkflowRuntimeException("必须指定一个要转换的类型");
		}
		activity.setTargetType(ognlString);
		activity.setInitValue(args);
	}

}
