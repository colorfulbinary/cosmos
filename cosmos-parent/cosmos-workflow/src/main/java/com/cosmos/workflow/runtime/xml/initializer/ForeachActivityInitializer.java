package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.logic.ForeachActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class ForeachActivityInitializer implements IXmlInitializer<ForeachActivity>{

	@Override
	public void init(ForeachActivity activity, Element item)
			throws WorkflowRuntimeException {
		String in = item.attributeValue("in");
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(in)){
			throw new WorkflowRuntimeException("foreach错误:必须指明一个输入集合！");
		}
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("foreach错误:必须指明一个输出集合！");
		}
		activity.setIn(in);
		activity.setOut(out);
		String exCont = item.attributeValue("ex-on-continue");
		if(exCont!=null && exCont.trim().equals("true")){
			activity.setExceptionOnContinue(true);
		}
	}

}
