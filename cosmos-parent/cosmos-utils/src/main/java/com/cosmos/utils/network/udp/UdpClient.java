package com.cosmos.utils.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cosmos.utils.network.NetworkException;

public class UdpClient {
	
	public static interface UdpProcessor{
		public void process(DatagramPacket dp);
	}
	
	private class UdpServerAcceptor implements Runnable{
		
		private DatagramPacket dp;
		
		private UdpProcessor processor;
		
		public UdpServerAcceptor(DatagramPacket dp,UdpProcessor processor){
			this.dp = dp;
			this.processor = processor;
		}
		
		@Override
		public void run() {
			this.processor.process(this.dp);
		}
	}
	
	private DatagramSocket ds;
	
	private UdpProcessor processor;
	
	private int receiveBufferedSize = 512;
		
	private ExecutorService pools = null;
	
	private int threadPoolSize = 3;
	
	public void setThreadPoolSize(int size){
		this.threadPoolSize = size;
	}
	
	public void setReceiveBufferedSize(int size){
		this.receiveBufferedSize = size;
	}
	
	public DatagramSocket getBaseSocket(){
		return this.ds;
	}
	
	public UdpClient(String address,int port,UdpProcessor processor) throws NetworkException{
		try {
			this.ds = new DatagramSocket(port, Inet4Address.getByName(address));
			this.processor = processor;
		} catch (Exception e) {
			throw new NetworkException(e.getMessage(), e);
		} 
		this.pools = Executors.newFixedThreadPool(this.threadPoolSize);
	}
	
	public UdpClient(String address,int port) throws NetworkException{
		this(address, port, null);
	}
	
	public UdpClient() throws NetworkException{
		try {
			this.ds = new DatagramSocket();
		} catch (SocketException e) {
			throw new NetworkException(e.getMessage(), e);
		}
		//this.pools = Executors.newFixedThreadPool(this.threadPoolSize);
	}
	
	public void listen(){
		while (true) {
			byte[] buf = new byte[this.receiveBufferedSize]; 
			DatagramPacket redpt = new DatagramPacket(buf, this.receiveBufferedSize);
			try {
				this.ds.receive(redpt);
				if(this.processor != null){
					this.pools.execute(new UdpServerAcceptor(redpt,this.processor));
				}
			} catch (IOException e) {
			}
		}
	}
	
	public void sendPacket(byte[] packet,String address,int port) throws NetworkException{
		try {
			DatagramPacket tdp=  new DatagramPacket(packet, packet.length,Inet4Address.getByName(address),port); 
			this.ds.send(tdp);
		} catch (IOException e) {
			throw new NetworkException(e.getMessage(),e);
		}
	}
	
	public void close(){
		this.ds.close();
	}

}
