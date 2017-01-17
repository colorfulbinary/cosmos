package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.xml.GetElementByXPathActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class GetElementByXPathActivityInitializer implements IXmlInitializer<GetElementByXPathActivity>{

	@Override
	public void init(GetElementByXPathActivity activity, Element item)
			throws WorkflowRuntimeException {
		String in = item.attributeValue("in");
		if(StringUtils.isEmptyOrNull(in)){
			throw new WorkflowRuntimeException("必须指定一个查找的基础节点!");
		}
		activity.setIn(in);
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指定一个输出位置!");
		}
		activity.setOut(out);
		String xpath = item.attributeValue("xpath");
		String cout = item.attributeValue("count-out");
		String first = item.attributeValue("is-first");
		if(!StringUtils.isEmptyOrNull(first) && first.trim().equals("true")){
			activity.setFirst(true);
		} else {
			activity.setFirst(false);
		}
		activity.setXpath(xpath);
		activity.setCountOut(cout);
	}

}
