package com.cosmos.workflow.runtime.xml.initializer;

import java.util.List;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.redis.NX_TYPE;
import com.cosmos.workflow.activities.sequence.action.redis.RedisSetHashDataActivity;
import com.cosmos.workflow.activities.sequence.action.redis.RedisSetHashDataActivity.FieldValue;
import com.cosmos.workflow.activities.sequence.action.redis.STRING_TYPE;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class RedisSetHashDataActivityInitializer implements IXmlInitializer<RedisSetHashDataActivity>{

	@Override
	public void init(RedisSetHashDataActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String key = item.attributeValue("key");
		String time = item.attributeValue("time");
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(client) ||
			StringUtils.isEmptyOrNull(key)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		
		@SuppressWarnings("unchecked")
		List<Element> elements = item.elements("field-info");
		FieldValue[] vls = new FieldValue[elements.size()];
		for (int i = 0,len = elements.size(); i < len; i++) {
			Element ele = elements.get(i);
			String fname = ele.attributeValue("field-name");
			String value = ele.attributeValue("value");
			String type = ele.attributeValue("type");
			String nxxx = ele.attributeValue("nxxx");
			if(StringUtils.isEmptyOrNull(fname) || StringUtils.isEmptyOrNull(value)){
				throw new WorkflowRuntimeException("field与value不能为空");
			}
			NX_TYPE nx = null;
			if(StringUtils.isEmptyOrNull(nxxx) || "NONE".equals(nxxx)){
				nx = NX_TYPE.NONE;
			} else {
				nx = NX_TYPE.NX;
			}
			STRING_TYPE tp = null;
			if(StringUtils.isEmptyOrNull(type) || "STRING".equals(type)){
				tp = STRING_TYPE.STRING;
			} else {
				tp = STRING_TYPE.BINARY;
			}
			vls[i] = activity.creatFieldValue(fname, value, nx, tp);
		}
		
		if(time != null){
			try {
				activity.setTime(new Long(time));
			} catch (NumberFormatException e) {
				activity.setTime(0);
			}
		}
		activity.setValue(vls);
		activity.setClient(client);
		activity.setKey(key);
		activity.setOut(out);
	}
	
}
