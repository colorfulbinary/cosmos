package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.RedisKeysActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisKeysActivityInitializer implements IXmlInitializer<RedisKeysActivity>{

	@Override
	public void init(RedisKeysActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String pattern = item.attributeValue("pattern");
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(client) ||
			StringUtils.isEmptyOrNull(pattern) ||
			StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		activity.setClient(client);
		activity.setPattern(pattern);
		activity.setOut(out);
	}
}
