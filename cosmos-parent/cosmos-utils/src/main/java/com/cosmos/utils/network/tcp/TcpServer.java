package com.cosmos.utils.network.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;

import com.cosmos.utils.network.NetworkException;
import com.cosmos.utils.network.NetworkSupport;
import com.cosmos.utils.network.ssl.SSLConfiguration;
import com.cosmos.utils.network.ssl.SSLConfiguration.SSLSocketConfiguration;

public class TcpServer extends NetworkSupport{
	
	public static interface TcpProcessor{
		public void process(TcpClient client);
	}
	
	private class TcpServerAcceptor implements Runnable{
		
		private TcpClient accept;
		
		private TcpProcessor processor;
		
		public TcpServerAcceptor(TcpClient accept,TcpProcessor processor){
			this.accept = accept;
			this.processor = processor;
		}
				
		@Override
		public void run() {
			try {
				this.processor.process(accept);
			} catch (Exception e) {
			}
		}	
	}
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private WeakHashMap<TcpClient, Long> mapping = new WeakHashMap<TcpClient, Long>(); 
	
	private ExecutorService pools = null;
	
	private ServerSocket listener = null;
	
	private ServerSocketFactory factory;
	
	private TcpProcessor processor;
	
	private int threadPoolSize = 3;
	
	private SSLConfiguration sslConfiguration;
	
	public TcpServer(TcpProcessor processor) throws NetworkException{
		this(null,processor);
	}
	
	public TcpServer(SSLConfiguration sslConfiguration,TcpProcessor processor) throws NetworkException{
		this.pools = Executors.newFixedThreadPool(this.threadPoolSize);
		this.processor = processor;
		if(sslConfiguration != null){
			this.sslConfiguration = sslConfiguration;
			this.factory = this.getSSLContext(sslConfiguration).getServerSocketFactory();
		} else {
			this.factory = ServerSocketFactory.getDefault(); 
		}
		
	}
	
	public void setThreadPoolSize(int size){
		this.threadPoolSize = size;
	}
	
	public Set<TcpClient> getAllClient(){
		Set<TcpClient> result = new HashSet<TcpClient>();
		this.lock.readLock().lock();
		Set<TcpClient> keySet = this.mapping.keySet();
		for (TcpClient item : keySet) {
			result.add(item);
		}
		this.lock.readLock().unlock();
		return result;
	}
	
	private void bind(InetAddress address,int port,int timeout) throws IOException{
		this.listener = this.factory.createServerSocket();
		this.listener.setSoTimeout(timeout);
		if(this.listener instanceof SSLServerSocket){
			SSLServerSocket sslServer = (SSLServerSocket) this.listener;
			SSLSocketConfiguration socketConfiguration = this.sslConfiguration.getSocketConfiguration();
			if(socketConfiguration!=null){
				String[] enabledCipherSuites = socketConfiguration.enabledCipherSuites();
				String[] enabledProtocols = socketConfiguration.enabledProtocols();
				boolean enableSessionCreation = socketConfiguration.enableSessionCreation();
				boolean checkClient = socketConfiguration.checkClientAuth();
				boolean useClientMode = socketConfiguration.useClientMode();
				sslServer.setEnableSessionCreation(enableSessionCreation);
				if(enabledCipherSuites!=null){
					sslServer.setEnabledCipherSuites(enabledCipherSuites);
				}
				if(enabledProtocols != null){
					sslServer.setEnabledProtocols(enabledProtocols);
				}
				sslServer.setUseClientMode(useClientMode);
				if(checkClient){
					sslServer.setNeedClientAuth(true);
				} else {
					sslServer.setWantClientAuth(true);
				}
			} 
		}
		this.listener.bind(new InetSocketAddress(address, port));
		
	}
	
	public void listen(InetAddress address,int port) throws IOException{
		if(this.listener != null && !this.listener.isClosed()){
			throw new IOException("存在没有关闭的");
		}
		this.bind(address, port,0);
		while(true){
			Socket accept = this.listener.accept();
			TcpClient client = new TcpClient(accept);
			lock.writeLock().lock();
			mapping.put(client, System.currentTimeMillis());
			lock.writeLock().unlock();
			pools.execute(new TcpServerAcceptor(client, this.processor));
		}
	}
	
	
	
	public synchronized TcpClient listenOnce(InetAddress address,int port,int timeout) throws IOException{
		if(this.listener != null && !this.listener.isClosed()){
			throw new IOException("存在没有关闭的");
		}
		TcpClient result = null;
		this.bind(address, port,timeout);
		Socket accept = null;
		try{
			accept = this.listener.accept();
			result = new TcpClient(accept);
		} catch (Exception ex){
		} finally{
			if(this.listener!= null && !this.listener.isClosed()){
				this.listener.close();
				this.listener = null;
			}
		}
		if(result != null){
			lock.writeLock().lock();
			mapping.put(result, System.currentTimeMillis());
			lock.writeLock().unlock();
			pools.execute(new TcpServerAcceptor(result, this.processor));
		}
		return result;
	}
	
	public void stop(){
		try {
			if(!this.listener.isClosed()){
				this.listener.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) throws IOException {
//		SSLConfiguration config = new X509CerFileSSLConfiguration(new File("D:\\temp\\tserver.keystore"), new File("D:\\temp\\kserver.keystore"), "hee123", "hee123", "hee123");
//		TcpServer server = new TcpServer(config,new DefaultTcpProcessor());
//		server.listen(InetAddress.getByName("192.168.234.1"), 100, 9527);
//	}
}
