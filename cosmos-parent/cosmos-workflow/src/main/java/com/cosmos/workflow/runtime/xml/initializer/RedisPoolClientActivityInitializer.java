package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.RedisPoolClientActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisPoolClientActivityInitializer implements IXmlInitializer<RedisPoolClientActivity>{
	
	@Override
	public void init(RedisPoolClientActivity activity, Element item)
			throws WorkflowRuntimeException {
			String poolname = item.attributeValue("pool-name");
			String out = item.attributeValue("out");
			String index = item.attributeValue("index");
			if(StringUtils.isEmptyOrNull(poolname) || StringUtils.isEmptyOrNull(out)){
				throw new WorkflowRuntimeException("必填项不能为空");
			}
			if(index != null){
				try {
					activity.setIndex(new Integer(index));
				} catch (NumberFormatException e) {
					activity.setIndex(0);
				}
			}
			activity.setPoolName(poolname);
			activity.setOut(out);
	}

}
