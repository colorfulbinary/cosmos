package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.NX_TYPE;
import com.cosmos.workflow.activities.sequence.action.redis.RedisSetDataActivity;
import com.cosmos.workflow.activities.sequence.action.redis.STRING_TYPE;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisSetDataActivityInitializer implements IXmlInitializer<RedisSetDataActivity>{

	@Override
	public void init(RedisSetDataActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String key = item.attributeValue("key");
		String type = item.attributeValue("type");
		String value = item.attributeValue("value");
		String nxxx = item.attributeValue("nxxx");
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
		if("NX".equals(nxxx)){
			activity.setNxxx(NX_TYPE.NX);
		} else if("XX".equals(nxxx)){
			activity.setNxxx(NX_TYPE.XX);
		} else {
			activity.setNxxx(NX_TYPE.NONE);
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
