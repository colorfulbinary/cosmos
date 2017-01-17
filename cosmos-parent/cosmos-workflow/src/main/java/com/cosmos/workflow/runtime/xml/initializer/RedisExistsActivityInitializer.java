package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.RedisExistsActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisExistsActivityInitializer implements IXmlInitializer<RedisExistsActivity>{

	@Override
	public void init(RedisExistsActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String key = item.attributeValue("key");
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(client) ||
			StringUtils.isEmptyOrNull(out) ||
			StringUtils.isEmptyOrNull(key)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		activity.setClient(client);
		activity.setKey(key);
		activity.setOut(out);
	}

}
