package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.db.DBRollbackTransactionActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class DBRollbackTransactionActivityInitializer implements IXmlInitializer<DBRollbackTransactionActivity>{

	@Override
	public void init(DBRollbackTransactionActivity activity, Element item)
			throws WorkflowRuntimeException {
		String access = item.attributeValue("access");
		if(access == null || access.trim().equals("")){
			throw new WorkflowRuntimeException("必须指定一个DBAccess");
		} else {
			activity.setDbAccess(access);
		}
	}

}
