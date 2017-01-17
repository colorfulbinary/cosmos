package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.state.StateActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class StateActivityInitializer implements IXmlInitializer<StateActivity>{

	@Override
	public void init(StateActivity activity, Element item)
			throws WorkflowRuntimeException {
		String id = item.attributeValue("id");
		if(id == null){
			throw new WorkflowRuntimeException("所有状态节点必须有ID属性");
		}
		activity.setActivityId(id);
		Element initEle = item.element("initialize");
		if(initEle!=null){
			String initId = initEle.attributeValue("ref");
			activity.setInitializer(initId);
		}
		String type = item.attributeValue("type");
		int typeValue = -1;
		if(type == null || "normal".equals(type.trim())){
			typeValue = StateActivity.NORMAL;
		} else if("begin".equals(type.trim())){
			typeValue = StateActivity.BEGIN;
		} else if("end".equals(type.trim())){
			typeValue = StateActivity.END;
		} else {
			throw new WorkflowRuntimeException("无法状态节点类型TYPE:" + type.trim());
		}
		activity.setType(typeValue);
	}

}
