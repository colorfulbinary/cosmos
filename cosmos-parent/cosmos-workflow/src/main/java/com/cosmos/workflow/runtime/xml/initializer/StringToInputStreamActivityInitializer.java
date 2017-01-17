package com.cosmos.workflow.runtime.xml.initializer;
import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.convertor.StringToInputStreamActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;


public class StringToInputStreamActivityInitializer implements IXmlInitializer<StringToInputStreamActivity>{
	
	@Override
	public void init(StringToInputStreamActivity activity, Element item)
			throws WorkflowRuntimeException {
		String in = item.attributeValue("in");
		if(StringUtils.isEmptyOrNull(in)){
			throw new WorkflowRuntimeException("必须指定一个要转换的字符串!");
		}
		activity.setIn(in);
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指定一个输出位置!");
		}
		activity.setOut(out);
		String id = item.attributeValue("id");
		String charset = item.attributeValue("charset");
		if(charset == null || charset.trim().equals("")){
			charset = "utf-8";
		}
		if (in == null || in.trim().equals("")) {
			throw new WorkflowRuntimeException("没有配置要转换的KEY值");
		}
		activity.setActivityId(id);
		activity.setCharset(charset);
	}

}
