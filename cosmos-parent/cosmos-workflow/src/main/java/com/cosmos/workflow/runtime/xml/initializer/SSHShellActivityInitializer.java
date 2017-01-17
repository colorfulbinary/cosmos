package com.cosmos.workflow.runtime.xml.initializer;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.network.ssh.SSHShellActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class SSHShellActivityInitializer implements IXmlInitializer<SSHShellActivity>{

	@Override
	public void init(SSHShellActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String charset = item.attributeValue("charset");
		String processor = item.attributeValue("processor");
		String timeout = item.attributeValue("timeout");
		String endChars = item.attributeValue("endChars");
		if(StringUtils.isEmptyOrNull(endChars)){
			throw new WorkflowRuntimeException("必须指明至少一个结束符");
		}
		activity.setEndChars(endChars.split(","));
		if(StringUtils.isEmptyOrNull(client)){
			throw new WorkflowRuntimeException("必须指明至少一个client");
		}
		activity.setClient(client);
		if(StringUtils.isEmptyOrNull(charset)){
			charset = "UTF-8";
		}
		activity.setCharset(charset);
		activity.setProcessor(processor);
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
		@SuppressWarnings("unchecked")
		List<Element> commands = item.elements("ssh-command");
		for (int i = 0,len = commands.size(); i < len; i++) {
			Element commandItem = commands.get(i);
			String args = commandItem.attributeValue("arguments");
			String out = commandItem.attributeValue("out");
			String command = commandItem.getTextTrim();
			String[] arguments = StringUtils.isEmptyOrNull(args) ? new String[0] : args.split(",");
			if(StringUtils.isEmptyOrNull(command)){
				throw new WorkflowRuntimeException("ssh-command:命令不能为空!");
			}
			if(StringUtils.isEmptyOrNull(out)){
				throw new WorkflowRuntimeException("ssh-command:必须!");
			}
			activity.appendCommnad(command, out, arguments);
		}
	}

}
