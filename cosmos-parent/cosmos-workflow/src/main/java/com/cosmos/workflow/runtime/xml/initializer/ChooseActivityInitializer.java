package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.logic.ChooseActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class ChooseActivityInitializer implements IXmlInitializer<ChooseActivity>{

	@Override
	public void init(ChooseActivity activity, Element item)
			throws WorkflowRuntimeException {
		String only = item.attributeValue("only");
		if(only == null || !only.trim().equals("false")){
			activity.setOnly(true);
		} else {
			activity.setOnly(false);
		}
		String exCont = item.attributeValue("ex-on-continue");
		if(exCont!=null && exCont.trim().equals("true")){
			activity.setExceptionOnContinue(true);
		}
	}

}
