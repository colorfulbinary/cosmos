package com.cosmos.utils.network.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HttpsURLConnection;

import com.cosmos.utils.io.StreamProcessor;
import com.cosmos.utils.network.NetworkSupport;
import com.cosmos.utils.network.ssl.SSLConfiguration;

public abstract class HttpClient<I, O> extends NetworkSupport{
	
	protected Map<String, String> requestHeader = new ConcurrentHashMap<String, String>();
	
	public void putRequestHeader(String key,String value){
		requestHeader.put(key, value);
	}
	
	public void clearRequestHeader(){
		requestHeader.clear();
	}
	
	protected class HttpResponseHeaders{
		
		private String contentType;
		
		private int contentLength;
		
		private String contentEncoding;
		
		private Map<String, List<String>> responseHeaders;
		
		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public int getContentLength() {
			return contentLength;
		}

		public void setContentLength(int contentLength) {
			this.contentLength = contentLength;
		}

		public String getContentEncoding() {
			return contentEncoding;
		}

		public void setContentEncoding(String contentEncoding) {
			this.contentEncoding = contentEncoding;
		}

		public Map<String, List<String>> getResponseHeaders() {
			return responseHeaders;
		}

		public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
			this.responseHeaders = responseHeaders;
		}
		
	}
	
	protected abstract InputStream parseArgs(I input) throws HttpException;
	
	protected abstract O parseResult(InputStream input,HttpResponseHeaders headers) throws HttpException;
	
	private O request(HttpURLConnection connection,HttpMethod method,I in,StreamProcessor.Progress uploadProgress) throws HttpException{
		O result = null;
		for (Entry<String, String> item : this.requestHeader.entrySet()) {
			connection.setRequestProperty(item.getKey(), item.getValue());
		}
		try {
			connection.setRequestMethod(method.getHttpMethodName());
		} catch (ProtocolException e) {
			throw new HttpException("设置请求方法失败:" + e.getMessage(),e);
		}
		connection.setDoInput(true);
		connection.setDoOutput(true);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		InputStream args = null;
		try {
			args = this.parseArgs(in);
			if(args != null){
				outputStream = connection.getOutputStream();
				if(uploadProgress == null){
					StreamProcessor.forword(args, outputStream);
				} else {
					StreamProcessor.forword(args, outputStream,uploadProgress);
				}
				outputStream.flush();
			}
			if (connection.getResponseCode() >= 300) {
	            throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
	        }
			inputStream = connection.getInputStream();
			HttpResponseHeaders headers = new HttpResponseHeaders();
			headers.setContentEncoding(connection.getContentEncoding());
			headers.setContentLength(connection.getContentLength());
			headers.setContentEncoding(connection.getContentEncoding());
			headers.setResponseHeaders(connection.getHeaderFields());
			result = this.parseResult(inputStream,headers);
		} catch (Exception e) {
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			throw new HttpException("Http请求失败:" + e.getMessage(),e);
		} finally {
			if(args != null){
				try {
					args.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(outputStream != null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        return result;
	}
	
	public O sendHttpRequest(String address,HttpMethod method,I in) throws HttpException{
		return this.sendHttpRequest(address, method, in, null);
	}
	
	public O sendHttpRequest(String address,HttpMethod method,I in,StreamProcessor.Progress uploadProgress) throws HttpException{
		URL targetUrl;
		try {
			targetUrl = new URL(address);
		} catch (MalformedURLException e) {
			throw new HttpException("无法解析传入的HTTP地址:" + e.getMessage(),e);
		}
		String protocol = targetUrl.getProtocol();
		if(!protocol.equalsIgnoreCase("http") ){
			throw new HttpException("不支持HTTP以外的协议!");
		}
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) targetUrl.openConnection();
		} catch (IOException e) {
			throw new HttpException("打开HTTP连接失败:" + e.getMessage(),e);
		}
        return this.request(connection, method, in, uploadProgress);
	}
	
	public O sendSSLHttpRequest(String address,HttpMethod method,I in,SSLConfiguration sslConfiguration) throws HttpException{
		return this.sendSSLHttpRequest(address, method, in, sslConfiguration,null);
	}
	
	public O sendSSLHttpRequest(String address,HttpMethod method,I in,SSLConfiguration sslConfiguration,StreamProcessor.Progress uploadProgress) throws HttpException{
		URL targetUrl;
		try {
			targetUrl = new URL(address);
		} catch (MalformedURLException e) {
			throw new HttpException("无法解析传入的HTTP地址:" + e.getMessage(),e);
		}
		String protocol = targetUrl.getProtocol();
		if(!protocol.equalsIgnoreCase("https")){
			throw new HttpException("不支持HTTPS以外的协议!");
		}
		HttpsURLConnection connection;
		try {
			connection = (HttpsURLConnection) targetUrl.openConnection();
			connection.setSSLSocketFactory(this.getSSLContext(sslConfiguration).getSocketFactory());
		} catch (IOException e) {
			throw new HttpException("打开HTTPS连接失败:" + e.getMessage(),e);
		}
        return this.request(connection, method, in, uploadProgress);
	}
}
