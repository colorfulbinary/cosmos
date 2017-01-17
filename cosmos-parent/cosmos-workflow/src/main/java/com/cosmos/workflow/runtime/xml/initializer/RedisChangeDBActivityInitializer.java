package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.RedisChangeDBActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisChangeDBActivityInitializer implements IXmlInitializer<RedisChangeDBActivity>{

	@Override
	public void init(RedisChangeDBActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String index = item.attributeValue("index");
		if(StringUtils.isEmptyOrNull(client) ||
			StringUtils.isEmptyOrNull(index)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		try {
			int indexVal = Integer.valueOf(index);
			activity.setIndex(indexVal);
		} catch (NumberFormatException e) {
			throw new WorkflowRuntimeException("无法解析数据库索引号");
		}
		activity.setClient(client);
	}

}
