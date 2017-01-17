package com.cosmos.workflow.runtime.xml;

import org.dom4j.Element;

import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;

public interface IXmlInitializer<T extends Activity> {	

	public void init(T activity,Element item) throws WorkflowRuntimeException;
}
