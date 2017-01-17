package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.convertor.InputStreamToJsonMapActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class InputStreamToJsonMapActivityInitializer implements IXmlInitializer<InputStreamToJsonMapActivity>{

	@Override
	public void init(InputStreamToJsonMapActivity activity, Element item)
			throws WorkflowRuntimeException {
		String id = item.attributeValue("id");
		activity.setActivityId(id);
		String in = item.attributeValue("in");
		if(StringUtils.isEmptyOrNull(in)){
			throw new WorkflowRuntimeException("必须指明一个输入流");
		}
		activity.setIn(in);
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指明一个输出位置");
		}
		activity.setOut(out);
	}

}
