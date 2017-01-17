package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.LIST_DIRECTION;
import com.cosmos.workflow.activities.sequence.action.redis.RedisListPopActivity;
import com.cosmos.workflow.activities.sequence.action.redis.STRING_TYPE;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisListPopActivityInitializer implements IXmlInitializer<RedisListPopActivity>{

	@Override
	public void init(RedisListPopActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String key = item.attributeValue("key");
		String type = item.attributeValue("type");
		String direction = item.attributeValue("direction");
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(client) ||
			StringUtils.isEmptyOrNull(key)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		if(StringUtils.isEmptyOrNull(type) || type.equals("STRING")){
			activity.setType(STRING_TYPE.STRING);
		} else {
			activity.setType(STRING_TYPE.BINARY);
		}
		if("LEFT".equals(direction)){
			activity.setDirection(LIST_DIRECTION.LEFT);
		} else {
			activity.setDirection(LIST_DIRECTION.RIGHT);
		}
		activity.setClient(client);
		activity.setKey(key);
		activity.setOut(out);
	}

}
