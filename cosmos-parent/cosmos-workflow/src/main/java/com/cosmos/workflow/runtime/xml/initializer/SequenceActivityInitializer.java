package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class SequenceActivityInitializer implements IXmlInitializer<SequenceActivity>{

	@Override
	public void init(SequenceActivity activity, Element item)
			throws WorkflowRuntimeException {
		String timeoutString = item.attributeValue("timeout");
		//System.out.println(timeoutString);
		long timeout = -1;
		if(timeoutString != null && !"".equals(timeoutString.trim())){
			try {
				timeout = Long.parseLong(timeoutString);
			} catch (NumberFormatException e) {
			}
		}
		activity.setTimeout(timeout);
		String exCont = item.attributeValue("ex-on-continue");
		if(exCont!=null && exCont.trim().equals("true")){
			activity.setExceptionOnContinue(true);
		}
	}

}
