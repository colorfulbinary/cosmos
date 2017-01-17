package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.RedisClientActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisClientActivityInitializer implements IXmlInitializer<RedisClientActivity>{

	@Override
	public void init(RedisClientActivity activity, Element item)
			throws WorkflowRuntimeException {
		String host = item.attributeValue("host");
		String port = item.attributeValue("port");
		String password = item.attributeValue("password");
		String timeout = item.attributeValue("timeout");
		String out = item.attributeValue("out");
		String index = item.attributeValue("index");
		if(StringUtils.isEmptyOrNull(host) ||
			StringUtils.isEmptyOrNull(port) ||
			StringUtils.isEmptyOrNull(password) ||
			StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		if(timeout != null){
			try {
				activity.setTimeout(new Integer(timeout));
			} catch (NumberFormatException e) {
				activity.setTimeout(0);
			}
		}
		if(index != null){
			try {
				activity.setIndex(new Integer(index));
			} catch (NumberFormatException e) {
				activity.setIndex(0);
			}
		}
		activity.setHost(host);
		activity.setOut(out);
		activity.setPassword(password);
		activity.setPort(port);
		
	}

}
