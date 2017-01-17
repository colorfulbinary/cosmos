package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.call.StringFormatActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class StringFormatActivityInitializer implements IXmlInitializer<StringFormatActivity>{

	@Override
	public void init(StringFormatActivity activity, Element item)
			throws WorkflowRuntimeException {
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指明一个输出!");
		}
		activity.setOut(out);
		activity.setSource(item.attributeValue("source"));
		String args = item.attributeValue("arguments");
		if(StringUtils.isEmptyOrNull(args)){
			activity.setArgs(new String[0]);
		} else {
			activity.setArgs(args.split(","));
		}
	}

}
