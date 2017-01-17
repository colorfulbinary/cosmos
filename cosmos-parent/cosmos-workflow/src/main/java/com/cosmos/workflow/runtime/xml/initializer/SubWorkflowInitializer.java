package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.call.SubWorkflowActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class SubWorkflowInitializer implements IXmlInitializer<SubWorkflowActivity>{

	@Override
	public void init(SubWorkflowActivity activity, Element item)
			throws WorkflowRuntimeException {
		String flowId = item.attributeValue("ref");
		activity.setRef(flowId);
	}

}
