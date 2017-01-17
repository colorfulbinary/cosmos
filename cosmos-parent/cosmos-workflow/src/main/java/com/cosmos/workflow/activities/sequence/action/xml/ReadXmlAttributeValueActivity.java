package com.cosmos.workflow.activities.sequence.action.xml;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class ReadXmlAttributeValueActivity extends ActionActivity{

	private static final long serialVersionUID = -4408163669915734384L;
	
	private String attributeName;
	
	
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}


	@Override
	public void release() {
		this.attributeName = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		if(StringUtils.isEmptyOrNull(this.in)){
			throw new WorkflowException(this, "必须指定一个Element");
		}
//		if(StringUtils.isEmptyOrNull(this.attributeName)){
//			throw new WorkflowException(this, "必须指定一个属性名");
//		}
		Object ele = data.get(this.in);
		if(ele != null && ele instanceof Element){
			Element item = (Element) ele;
			String result = null;
			if(StringUtils.isEmptyOrNull(this.attributeName)){
				result = item.getTextTrim();
			} else {
				result = item.attributeValue(this.attributeName);
			}
			data.put(this.out, result);
		} else {
			throw new WorkflowException(this, "必须指定一个Element的非空对象");
		}
		
	}
	
}
