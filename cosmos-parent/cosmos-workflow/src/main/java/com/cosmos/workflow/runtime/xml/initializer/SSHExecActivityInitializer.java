package com.cosmos.workflow.runtime.xml.initializer;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.network.ssh.SSHExecActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class SSHExecActivityInitializer implements IXmlInitializer<SSHExecActivity>{

	@Override
	public void init(SSHExecActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String charset = item.attributeValue("charset");
		String out = item.attributeValue("out");
		String arguments = item.attributeValue("arguments");
		String timeout = item.attributeValue("timeout");
		String command = item.getTextTrim();
		if(StringUtils.isEmptyOrNull(client) ||
		    StringUtils.isEmptyOrNull(charset) ||
			StringUtils.isEmptyOrNull(out) ||
			StringUtils.isEmptyOrNull(command)){
			throw new WorkflowRuntimeException("必填项不能为空!");
		}
		activity.setCharset(charset);
		activity.setClient(client);
		activity.setOut(out);
		activity.setCommand(command);
		if(StringUtils.isEmptyOrNull(arguments)){
			activity.setArguments(new String[0]);
		} else {
			activity.setArguments(arguments.split(","));
		}
		if(timeout != null){
			try {
				activity.setTimeout(new Integer(timeout));
			} catch (NumberFormatException e) {
				activity.setTimeout(0);
			}
		}
		Element config = item.element("ssh-environment");
		if(config != null){
			@SuppressWarnings("unchecked")
			List<Element> properties = config.elements("property");
			if(properties.size() !=0){
				HashMap<String, String> pr = new HashMap<String, String>();
				for (Element property : properties) {
					pr.put(property.attributeValue("key"), property.attributeValue("value"));
				}
				activity.setEnv(pr);
			}
		}
	}

}
