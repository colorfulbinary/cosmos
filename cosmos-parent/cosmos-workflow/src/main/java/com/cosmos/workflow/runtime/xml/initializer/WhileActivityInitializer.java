package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.logic.WhileActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class WhileActivityInitializer implements IXmlInitializer<WhileActivity>{

	@Override
	public void init(WhileActivity activity, Element item)
			throws WorkflowRuntimeException {
		Element express = item.element("express");
		if(express == null || "".equals(express.getTextTrim())){
			throw new WorkflowRuntimeException("while活动缺失表达式!");
		} else {
			activity.setCondition(express.getTextTrim());
			activity.setImportString(express.attributeValue("import"));
		}
		express.addAttribute("is-parse", "true");
		String exCont = item.attributeValue("ex-on-continue");
		if(exCont!=null && exCont.trim().equals("true")){
			activity.setExceptionOnContinue(true);
		}
	}

}
