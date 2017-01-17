package com.cosmos.utils.network;

import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.cosmos.utils.network.ssl.SSLConfiguration;

public abstract class NetworkSupport {
	
		
	protected SSLContext getSSLContext(SSLConfiguration sslConfiguration) throws NetworkException{
		if(sslConfiguration == null){
			throw new NetworkException("无法找到SSL相关配置");
		}
		String protocol = sslConfiguration.getProtocol();
		protocol = protocol == null ? "SSL" : protocol;
		String provider = sslConfiguration.getProvider();
		provider = provider == null ? "SunJSSE" : provider;
		SSLContext ctx;
		KeyManager[] keys = sslConfiguration.getKeyManagers();
		TrustManager[] trusts =  sslConfiguration.getTrustManagers();
		try {
			ctx = SSLContext.getInstance(protocol,provider);
			ctx.init(keys, trusts, new SecureRandom());
		} catch (Exception e) {
			throw new NetworkException("SSL错误:" + e.getMessage(),e);
		}
		return ctx;
	}
	
	private boolean reuseAddress;
	
	private int timeout;
	
	private int receiveBufferSize;
	
	public boolean isReuseAddress() {
		return reuseAddress;
	}

	public void setReuseAddress(boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}
}
