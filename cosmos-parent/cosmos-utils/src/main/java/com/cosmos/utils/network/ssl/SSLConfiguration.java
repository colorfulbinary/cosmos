package com.cosmos.utils.network.ssl;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

public interface SSLConfiguration {
	
	public static interface SSLSocketConfiguration {
		public String[] enabledCipherSuites();
		public String[] enabledProtocols();
		public boolean enableSessionCreation();
		public boolean checkClientAuth();
		public boolean useClientMode();
	} 
	public String getProtocol();
	
	public String getProvider();
	
	public KeyManager[] getKeyManagers() throws SSLException;
	
	public TrustManager[] getTrustManagers() throws SSLException;
	
	public SSLSocketConfiguration getSocketConfiguration();

}
