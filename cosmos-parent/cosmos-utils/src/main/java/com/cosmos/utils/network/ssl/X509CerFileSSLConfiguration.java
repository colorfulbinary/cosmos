package com.cosmos.utils.network.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class X509CerFileSSLConfiguration implements SSLConfiguration {

	private File trustCerFile;

	private String trustCerPwd;

	private File keyCerFile;

	private String keyCerPwd;
	
	private String keyCreFilePwd;


	public X509CerFileSSLConfiguration(File trustCerFile, File keyCerFile,
			String trustCerPwd, String keyCerPwd, String cerPwd) {
		this.trustCerFile = trustCerFile;
		this.keyCerFile = keyCerFile;
		this.trustCerPwd = trustCerPwd;
		this.keyCerPwd = keyCerPwd;
		this.keyCreFilePwd = cerPwd;
	}

	@Override
	public KeyManager[] getKeyManagers() throws SSLException {
		KeyManager[] result = null;
		try {
			if (this.keyCerFile != null && this.keyCerFile.exists()) {
				KeyStore keyStore = KeyStore.getInstance("JKS");
				keyStore.load(new FileInputStream(keyCerFile),
						this.keyCreFilePwd.toCharArray());
				KeyManagerFactory keyManagerFactory = KeyManagerFactory
						.getInstance("SunX509");
				keyManagerFactory.init(keyStore, keyCerPwd.toCharArray());
				result = keyManagerFactory.getKeyManagers();
			}
		} catch (Exception e) {
			throw new SSLException("获取信任管理错误:" + e.getMessage(),e);
		}
		return result;
	}

	@Override
	public TrustManager[] getTrustManagers() throws SSLException {
		TrustManager[] result = null;
		try {
			if (this.trustCerFile != null && this.trustCerFile.exists()) {
				KeyStore keyStore = KeyStore.getInstance("JKS");
				keyStore.load(new FileInputStream(trustCerFile),
						trustCerPwd.toCharArray());
				TrustManagerFactory trustManagerFactory = TrustManagerFactory
						.getInstance("SunX509");
				trustManagerFactory.init(keyStore);
				result = trustManagerFactory.getTrustManagers();
			}
		} catch (Exception e) {
			throw new SSLException("获取信任管理错误:" + e.getMessage(),e);
		}
		return result;
	}

	@Override
	public SSLSocketConfiguration getSocketConfiguration() {
		return null;
	}

	@Override
	public String getProtocol() {
		return null;
	}

	@Override
	public String getProvider() {
		return null;
	}

}
