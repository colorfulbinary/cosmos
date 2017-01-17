package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.KEYS_TYPE;
import com.cosmos.workflow.activities.sequence.action.redis.RedisGetValuesByKeysActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisGetValuesByKeysActivityInitializer implements IXmlInitializer<RedisGetValuesByKeysActivity>{

	@Override
	public void init(RedisGetValuesByKeysActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String keys = item.attributeValue("keys");
		String out = item.attributeValue("out");
		String type = item.attributeValue("type");
		if(StringUtils.isEmptyOrNull(client) ||
			StringUtils.isEmptyOrNull(keys) ||
			StringUtils.isEmptyOrNull(type) ||
			StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		activity.setClient(client);
		activity.setKeys(keys);
		if("BINARY".equals(type)){
			activity.setType(KEYS_TYPE.BINARY);
		} else if("STRING".equals(type)){
			activity.setType(KEYS_TYPE.STRING);
		} else if("HASH-STRING".equals(type)){
			activity.setType(KEYS_TYPE.HASH_STRING);
		} else if("HASH-BINARY".equals(type)){
			activity.setType(KEYS_TYPE.HASH_BINARY);
		} else {
			activity.setType(KEYS_TYPE.STRING);
		}
		activity.setOut(out);
		
	}

}
