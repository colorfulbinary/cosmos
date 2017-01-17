package com.cosmos.utils.network.http;

public enum HttpMethod{
	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE");
	private String methodName;
	HttpMethod(String methodName){
		this.methodName = methodName;
	}
	public String getHttpMethodName(){
		return this.methodName;
	}
}
