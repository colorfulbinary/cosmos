package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.convertor.InputStreamToStringActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class InputStreamToStringActivityInitializer implements IXmlInitializer<InputStreamToStringActivity>{

	@Override
	public void init(InputStreamToStringActivity activity, Element item)
			throws WorkflowRuntimeException {
		String id = item.attributeValue("id");
		String in = item.attributeValue("in");
		if(StringUtils.isEmptyOrNull(in)){
			throw new WorkflowRuntimeException("必须指定一个输入流!");
		}
		activity.setIn(in);
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指定一个输出位置!");
		}
		activity.setOut(out);
		String charset = item.attributeValue("charset");
		String length = item.attributeValue("length");
		if(charset == null || charset.trim().equals("")){
			charset = "utf-8";
		}
		if(length == null || length.trim().equals("")){
			length = "0";
		}
		Integer lengthVal = null;
		try {
			lengthVal = Integer.valueOf(length);
		} catch (NumberFormatException e) {
			throw new WorkflowRuntimeException("转换的长度必须是一个数字");
		}
		activity.setActivityId(id);
		activity.setCharset(charset);
		activity.setLength(lengthVal);
	}
}
