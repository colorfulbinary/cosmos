package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.xml.ReadXmlAttributeValueActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class ReadXmlAttributeValueActivityInitializer implements IXmlInitializer<ReadXmlAttributeValueActivity>{

	@Override
	public void init(ReadXmlAttributeValueActivity activity, Element item)
			throws WorkflowRuntimeException {
		String in = item.attributeValue("in");
		if (StringUtils.isEmptyOrNull(in)) {
			throw new WorkflowRuntimeException("必须指明一个Element");
		}
		activity.setIn(in);
		String out = item.attributeValue("out");
		if (StringUtils.isEmptyOrNull(in)) {
			throw new WorkflowRuntimeException("必须指明输出位置");
		}
		activity.setOut(out);
		activity.setAttributeName(item.attributeValue("attribute-name"));
	}

}
