package com.cosmos.utils.network.tcp;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

import com.cosmos.utils.io.StreamProcessor;
import com.cosmos.utils.network.NetworkSupport;
import com.cosmos.utils.network.ssl.SSLConfiguration;
import com.cosmos.utils.network.ssl.SSLConfiguration.SSLSocketConfiguration;

public class TcpClient extends NetworkSupport{
	
	private Socket client;
	
	private StreamProcessor processor;
	
	public Socket getBaseClient(){
		return this.client;
	}
	
	public SSLSocket getSSLBaseClient(){
		return (this.client instanceof SSLSocket) ? (SSLSocket)this.client : null;
	}
	
	public TcpClient(String address,int port) throws IOException{
		this.client = new Socket(address, port);
		this.initialize();
	}
	
	public TcpClient(SSLConfiguration sslConfiguration,String address,int port) throws IOException{
		if(sslConfiguration != null){
			SSLSocket sslClient = (SSLSocket) this.getSSLContext(sslConfiguration).getSocketFactory().createSocket(address, port);
			SSLSocketConfiguration socketConfiguration = sslConfiguration.getSocketConfiguration();
			if(socketConfiguration != null){
				String[] enabledCipherSuites = socketConfiguration.enabledCipherSuites();
				String[] enabledProtocols = socketConfiguration.enabledProtocols();
				boolean enableSessionCreation = socketConfiguration.enableSessionCreation();
				boolean checkClient = socketConfiguration.checkClientAuth();
				boolean useClientMode = socketConfiguration.useClientMode();
				sslClient.setEnableSessionCreation(enableSessionCreation);
				if(enabledCipherSuites!=null){
					sslClient.setEnabledCipherSuites(enabledCipherSuites);
				}
				if(enabledProtocols != null){
					sslClient.setEnabledProtocols(enabledProtocols);
				}
				sslClient.setUseClientMode(useClientMode);
				if(checkClient){
					sslClient.setNeedClientAuth(true);
				} else {
					sslClient.setWantClientAuth(true);
				}
			}
			this.client = sslClient;
		} else {
			this.client = new Socket(address, port);
		}
		this.initialize();
	}
	
	TcpClient(Socket client) throws IOException{
		this.client = client;
		this.initialize();
	}
	
	private void initialize() throws IOException{
		this.processor = new StreamProcessor(this.client.getInputStream(), this.client.getOutputStream());
	}
	
	public StreamProcessor getProcessor(){
		return this.processor;
	}
	
	public void close() throws IOException{
		try {
			this.processor.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			this.client.close();
		}
	}
	
//	public static void main(String[] args) throws IOException, SignedTypeFormatException {
//		SSLConfiguration config = new X509CerFileSSLConfiguration(new File("D:\\temp\\tclient.keystore"), new File("D:\\temp\\kclient.keystore"), "hee123", "hee123", "hee123");
//		TcpClient client = new TcpClient(config,"192.168.234.1", 9527);
//		StreamProcessor pr = client.getProcessor();
//		pr.writeInt32(333123);
//		client.close();
//	}
	
}
