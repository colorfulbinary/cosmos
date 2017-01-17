package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.RedisCloseActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisCloseActivityInitializer implements IXmlInitializer<RedisCloseActivity>{

	@Override
	public void init(RedisCloseActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		if(StringUtils.isEmptyOrNull(client)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		activity.setClient(client);
	}
}
