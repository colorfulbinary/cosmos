package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.logic.WhenActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class WhenActivityInitializer implements IXmlInitializer<WhenActivity>{

	@Override
	public void init(WhenActivity activity, Element item)
			throws WorkflowRuntimeException {
		String name = item.getName();
		if("other".equals(name)){
			activity.setType(WhenActivity.OTHER);
		} else {
			activity.setType(WhenActivity.WHEN);
			Element express = item.element("express");
			if(express == null || "".equals(express.getTextTrim())){
				throw new WorkflowRuntimeException("when活动缺失表达式!");
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

}
