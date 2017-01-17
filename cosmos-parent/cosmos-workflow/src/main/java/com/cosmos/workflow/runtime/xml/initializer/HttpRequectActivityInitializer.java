package com.cosmos.workflow.runtime.xml.initializer;

import java.util.List;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.network.http.HttpRequestActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class HttpRequectActivityInitializer implements IXmlInitializer<HttpRequestActivity>{

	@Override
	public void init(HttpRequestActivity activity, Element item)
			throws WorkflowRuntimeException {
		String id = item.attributeValue("id");
		activity.setActivityId(id);
		String in = item.attributeValue("in");
		if(StringUtils.isEmptyOrNull(in)){
			throw new WorkflowRuntimeException("必须指定输入参数!");
		}
		activity.setIn(in);
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指定一个输出位置!");
		}
		activity.setOut(out);
		String targetUrl = item.attributeValue("target");
		if(targetUrl!=null && !targetUrl.trim().equals("")){
			activity.setTargetUrl(targetUrl);
		}
		String method = item.attributeValue("method");
		if(method!=null && !method.trim().equals("")){
			activity.setMethod(method);
		}
		Element rp = item.element("request-properties");
		if (rp!=null) {
			@SuppressWarnings("unchecked")
			List<Element> properties = rp.elements("property");
			for (Element ele : properties) {
				String key = ele.attributeValue("key");
				String value = ele.attributeValue("value");
				activity.setRequestProperty(key, value);
			}
		}
		String client = item.attributeValue("client");
		if(client!=null && !client.trim().equals("")){
			activity.setClient(client);
		}
		String sslConfiguration = item.attributeValue("ssl");
		if(sslConfiguration!=null && !sslConfiguration.trim().equals("")){
			activity.setSslConfiguration(sslConfiguration);
		}
		String progress = item.attributeValue("progress");
		if(progress!=null && !progress.trim().equals("")){
			activity.setProgress(progress);
		}
	}

}
