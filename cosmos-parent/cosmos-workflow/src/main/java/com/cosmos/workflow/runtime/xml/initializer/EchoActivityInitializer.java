package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.call.EchoActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class EchoActivityInitializer implements IXmlInitializer<EchoActivity>{

	@Override
	public void init(EchoActivity activity, Element item)
			throws WorkflowRuntimeException {
		String in = item.attributeValue("in");
		activity.setIn(in);
		String target = item.attributeValue("target");
		if(StringUtils.isEmptyOrNull(target)){
			target = "console";
		}
		int type = EchoActivity.CONSOLE;
		if("file".equals(target)){
			type = EchoActivity.FILE;
		} else if("all".equals(target)){
			type = EchoActivity.CONSOLE | EchoActivity.FILE;
		}
		activity.setType(type);
		
		String path = item.attributeValue("file-out");
		if(StringUtils.isEmptyOrNull(path) && (type & EchoActivity.FILE) == EchoActivity.FILE){
			throw new WorkflowRuntimeException("必须指明一个文件路径");
		}
		activity.setPath(path);
		
	}

}
