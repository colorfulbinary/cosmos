package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.call.CodeActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class CodeActivityInitializer implements IXmlInitializer<CodeActivity>{

	@Override
	public void init(CodeActivity activity, Element item)
			throws WorkflowRuntimeException {
		String express = item.getTextTrim();
		String importString = item.attributeValue("import");
		if(express == null){
			throw new WorkflowRuntimeException(item.attributeValue("id") + ":while节点缺失表达式!");
		}
		activity.setCode(express);
		activity.setImportString(importString);
//		@SuppressWarnings("unchecked")
//		List<Element> compensations = item.elements("compensation");
//		if(compensations.size() == 1){
//			CompensateActivity ca = new CompensateActivity();
//			SequenceWorkflowXmlParse.getInstance().loadActivities(ca, compensations.get(0));
//			activity.setCompensation(ca);
//		}		
	}

}
