package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.network.ssh.SSHCloseActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class SSHCloseActivityInitializer implements IXmlInitializer<SSHCloseActivity>{

	@Override
	public void init(SSHCloseActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		if(StringUtils.isEmptyOrNull(client)){
			throw new WorkflowRuntimeException("client不能位空");
		} else {
			activity.setClient(client);
		}
	}

}
