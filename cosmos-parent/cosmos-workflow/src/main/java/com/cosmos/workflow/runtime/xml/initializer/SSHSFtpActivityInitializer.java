package com.cosmos.workflow.runtime.xml.initializer;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.network.ssh.SSHSFtpActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class SSHSFtpActivityInitializer implements IXmlInitializer<SSHSFtpActivity>{

	@Override
	public void init(SSHSFtpActivity activity, Element item)
			throws WorkflowRuntimeException {
		String client = item.attributeValue("client");
		String processor = item.attributeValue("processor");
		String timeout = item.attributeValue("timeout");
		activity.setClient(client);
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
		List<Element> commands = item.elements();
		for (int i = 0,len = commands.size(); i < len; i++) {
			Element commandItem = commands.get(i);
			String tagName = commandItem.getName();
			if(tagName.equals("ssh-sftp-delete-dir")){
				String path = commandItem.attributeValue("path");
				activity.appendCommand(SSHSFtpActivity.DELETE_DIRECTORY, new Object[]{path});
			} else if(tagName.equals("ssh-sftp-delete-file")){
				String path = commandItem.attributeValue("path");
				activity.appendCommand(SSHSFtpActivity.DELETE_FILE, new Object[]{path});
			} else if(tagName.equals("ssh-sftp-make-dir")){
				String path = commandItem.attributeValue("path");
				activity.appendCommand(SSHSFtpActivity.MAKE_DIRECTORY, new Object[]{path});
			} else if(tagName.equals("ssh-sftp-upload")){
				String in = commandItem.attributeValue("in");
				String target = commandItem.attributeValue("target");
				activity.appendCommand(SSHSFtpActivity.UPLOAD_FILE, new Object[]{in,target});
			} else if(tagName.equals("ssh-sftp-download")){
				String in = commandItem.attributeValue("in");
				String target = commandItem.attributeValue("target");
				activity.appendCommand(SSHSFtpActivity.DOWNLOAD_FILE, new Object[]{in,target});
			} else if(tagName.equals("ssh-sftp-chown")){
				String uid = commandItem.attributeValue("uid");
				String path = commandItem.attributeValue("path");
				activity.appendCommand(SSHSFtpActivity.CH_OWNER, new Object[]{uid,path});
			} else if(tagName.equals("ssh-sftp-chgrp")){
				String gid = commandItem.attributeValue("gid");
				String path = commandItem.attributeValue("path");
				activity.appendCommand(SSHSFtpActivity.CH_GRP, new Object[]{gid,path});
			} else if(tagName.equals("ssh-sftp-chmod")){
				String permissions = commandItem.attributeValue("permissions");
				String path = commandItem.attributeValue("path");
				activity.appendCommand(SSHSFtpActivity.CH_MOD, new Object[]{permissions,path});
			}
		}
	}

}
