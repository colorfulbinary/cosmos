package com.cosmos.utils.network.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.cosmos.utils.io.StreamProcessor;

public class HttpClientUsingString extends HttpClient<String, String>{
	
	private String inputCharset = "UTF-8";
	
	private String outputCharset = "UTF-8";
	
	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getOutputCharset() {
		return outputCharset;
	}

	public void setOutputCharset(String outputCharset) {
		this.outputCharset = outputCharset;
	}

	@Override
	protected InputStream parseArgs(String input) throws HttpException {
		InputStream result = null;
		if(input!=null && !input.equals("")){
			try {
				result = new ByteArrayInputStream(input.getBytes(this.inputCharset));
			} catch (UnsupportedEncodingException e) {
				throw new HttpException("字符串转输入流失败:" + e.getMessage(),e);
			}
		}
		return result;
	}

	@Override
	protected String parseResult(InputStream input,HttpResponseHeaders headers) throws HttpException{
		String result;
		try {
			byte[] bytes = StreamProcessor.readByte(input);
			String charset = headers.getContentEncoding();
			charset = charset == null ? this.outputCharset : charset;
			result = new String(bytes,charset);
		} catch (Exception e) {
			throw new HttpException("输入流转字符串失败:" + e.getMessage(),e);
		}finally{
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
//	public static void main(String[] args) throws HttpException {
//		HttpClientUsingString client = new HttpClientUsingString();
//		client.setOutputCharset("GBK");
//		String sendHttpRequest = client.sendHttpRequest("http://www.163.com",HttpMethod.GET,null);
//		System.out.println(sendHttpRequest);
//	}
}
