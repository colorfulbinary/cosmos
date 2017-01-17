package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.io.CloseStreamActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class CloseStreamActivityInitializer implements IXmlInitializer<CloseStreamActivity>{

	@Override
	public void init(CloseStreamActivity activity, Element item)
			throws WorkflowRuntimeException {
		activity.setIn(item.attributeValue("in"));
	}

}
