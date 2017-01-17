package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.db.DBCloseConnectionActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class DBCloseConnectionActivityInitializer implements IXmlInitializer<DBCloseConnectionActivity>{

	@Override
	public void init(DBCloseConnectionActivity activity, Element item)
			throws WorkflowRuntimeException {
		String access = item.attributeValue("access");
		if(access == null || access.trim().equals("")){
			throw new WorkflowRuntimeException("必须指定一个DBAccess");
		} else {
			activity.setDbAccess(access);
		}
	}

}
