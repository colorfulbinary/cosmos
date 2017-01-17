package com.cosmos.workflow.activities.sequence.action.network.http;

import java.util.HashMap;
import java.util.Map;

import com.cosmos.utils.io.StreamProcessor.Progress;
import com.cosmos.utils.network.http.DefaultHttpClient;
import com.cosmos.utils.network.http.HttpClient;
import com.cosmos.utils.network.http.HttpException;
import com.cosmos.utils.network.http.HttpMethod;
import com.cosmos.utils.network.ssl.SSLConfiguration;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class HttpRequestActivity extends ActionActivity{
	
	private static final long serialVersionUID = -157300936994770977L;

	public static final String GET = "GET";
	
	public static final String POST = "POST";
	
	public static final String PUT = "PUT";
	
	public static final String DELETE = "DELETE";
	
	private String targetUrl;
	
	private String method;
	
	private String client;
	
	private String sslConfiguration;
	
	private String progress;
	
	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getSslConfiguration() {
		return sslConfiguration;
	}

	public void setSslConfiguration(String sslConfiguration) {
		this.sslConfiguration = sslConfiguration;
	}

	private Map<String, String> requestProperties = new HashMap<String, String>();
	
	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	
	public void setRequestProperty(String key,String value){
		requestProperties.put(key, value);
	}
	

	@Override
	public void release() {
		this.method = null;
		this.targetUrl = null;
		this.requestProperties.clear();
		this.requestProperties = null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object args = null;
		if(this.in != null && !this.in.trim().equals("")){
			args = data.get(this.in);
		}
		SSLConfiguration sslConfig = null;
		Object ssl = null;
		if(this.sslConfiguration!=null && (ssl = data.get(this.sslConfiguration))!= null && ssl instanceof SSLConfiguration){
			sslConfig = (SSLConfiguration) ssl;
		}
		String targetUrl;
		if(this.targetUrl.startsWith("{")){
			Object url = data.get(this.targetUrl.substring(1,this.targetUrl.length() - 1));
			if(url==null || url.toString().trim().equals("")){
				throw new WorkflowException(this,"找不到绑定地址");
			}	
			targetUrl = url.toString();
		} else {
			targetUrl = this.targetUrl;
		}
		HttpClient httpClient = null;
		Object obj = null;
		if(this.client != null && (obj = data.get(this.client))!= null && obj instanceof HttpClient<?, ?>){
			httpClient = (HttpClient) obj;
		} else {
			httpClient = new DefaultHttpClient();
		}
		for (Map.Entry<String, String> item : this.requestProperties.entrySet()) {
			httpClient.putRequestHeader(item.getKey(), item.getValue());
		}
		HttpMethod method = HttpMethod.GET;
		if(this.method!=null && !this.method.equals("")){
			if(this.method.toUpperCase().trim().equals("POST")){
				method = HttpMethod.POST;
			} else if(this.method.toUpperCase().trim().equals("PUT")){
				method = HttpMethod.PUT;
			} else if(this.method.toUpperCase().trim().equals("DELETE")){
				method = HttpMethod.DELETE;
			}
		}
		Progress progress = null;
		Object pgs = null;
		if(this.progress!=null && (pgs = data.get(this.progress))!= null && pgs instanceof Progress){
			progress = (Progress) pgs;
		}
		Object result = null;
		try {
			if(sslConfig == null){
				if(progress == null){
					result = httpClient.sendHttpRequest(targetUrl, method, args);
				} else {
					result = httpClient.sendHttpRequest(targetUrl, method, args,progress);
				}
			} else {
				if(progress == null){
					result = httpClient.sendSSLHttpRequest(targetUrl, method, args, sslConfig);
				} else {
					result = httpClient.sendSSLHttpRequest(targetUrl, method, args,sslConfig,progress);
				}
			}
		} catch (HttpException e) {
			throw new WorkflowException(this,"调用Http错误:" + e.getMessage(),e);
		}
		data.put(this.out, result);
	}
	
}
