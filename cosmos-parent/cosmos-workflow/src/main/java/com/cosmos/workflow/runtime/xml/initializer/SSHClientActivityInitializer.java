package com.cosmos.workflow.runtime.xml.initializer;

import java.util.List;
import java.util.Properties;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.network.ssh.SSHClientActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class SSHClientActivityInitializer implements IXmlInitializer<SSHClientActivity>{

	@Override
	public void init(SSHClientActivity activity, Element item)
			throws WorkflowRuntimeException {
		String isReuse = item.attributeValue("is-reuse");
		String host = item.attributeValue("host");
		String port = item.attributeValue("port");
		String account = item.attributeValue("account");
		String password = item.attributeValue("password");
		String timeout = item.attributeValue("timeout");
		String out = item.attributeValue("out");
		String userinfo = item.attributeValue("userinfo");
		activity.setReuse(StringUtils.isEmptyOrNull(isReuse) || isReuse.trim().equals("true"));
		if(StringUtils.isEmptyOrNull(host) ||
			StringUtils.isEmptyOrNull(port) ||
			StringUtils.isEmptyOrNull(account) ||
			StringUtils.isEmptyOrNull(password) ||
			StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必填项不能为空");
		}
		activity.setHost(host);
		activity.setPort(port);
		activity.setAccount(account);
		activity.setPassword(password);
		activity.setOut(out);
		if(timeout != null){
			try {
				activity.setTimeout(new Integer(timeout));
			} catch (NumberFormatException e) {
				activity.setTimeout(0);
			}
		}
		activity.setUserInfo(userinfo);
		Element config = item.element("ssh-configuration");
		if(config != null){
			@SuppressWarnings("unchecked")
			List<Element> properties = config.elements("property");
			if(properties.size() !=0){
				Properties pr = new Properties();
				for (Element property : properties) {
					pr.put(property.attributeValue("key"), property.attributeValue("value"));
				}
				activity.setConfig(pr);
			}
		}
	}

}
