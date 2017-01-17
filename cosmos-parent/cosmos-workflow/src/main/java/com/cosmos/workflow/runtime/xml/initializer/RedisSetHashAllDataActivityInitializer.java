package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.RedisSetHashAllDataActivity;
import com.cosmos.workflow.activities.sequence.action.redis.STRING_TYPE;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisSetHashAllDataActivityInitializer implements IXmlInitializer<RedisSetHashAllDataActivity>{

	@Override
	public void init(RedisSetHashAllDataActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String key = item.attributeValue("key");
		String type = item.attributeValue("type");
		String value = item.attributeValue("value");
		String time = item.attributeValue("time");
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(client) ||
			StringUtils.isEmptyOrNull(key) ||
			StringUtils.isEmptyOrNull(value)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		if(StringUtils.isEmptyOrNull(type) || type.equals("STRING")){
			activity.setType(STRING_TYPE.STRING);
		} else {
			activity.setType(STRING_TYPE.BINARY);
		}
		if(time != null){
			try {
				activity.setTime(new Long(time));
			} catch (NumberFormatException e) {
				activity.setTime(0);
			}
		}
		activity.setValue(value);
		activity.setClient(client);
		activity.setKey(key);
		activity.setOut(out);
	}

}
