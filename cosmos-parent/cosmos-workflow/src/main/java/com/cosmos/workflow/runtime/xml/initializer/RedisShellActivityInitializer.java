package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.RedisShellActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisShellActivityInitializer implements IXmlInitializer<RedisShellActivity>{

	@Override
	public void init(RedisShellActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String shell = item.attributeValue("shell");
		if(StringUtils.isEmptyOrNull(client) ||
			StringUtils.isEmptyOrNull(shell)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		activity.setClient(client);
		activity.setShell(shell);
	}

}
