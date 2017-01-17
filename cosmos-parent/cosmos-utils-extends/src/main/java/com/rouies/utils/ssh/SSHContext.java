package com.rouies.utils.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.cosmos.utils.io.StreamProcessor;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class SSHContext {
	
	private JSch jsch;
	
	private ThreadLocal<Session> reuseSession= new ThreadLocal<Session>();
	
	private Session localSession;
	
	private boolean isReuse;
	
	
//	private class DefaultUserInfo implements UserInfo{
//		
//		private String password;
//		
//		public DefaultUserInfo(String password){
//			this.password = password;
//		}
//
//		@Override
//		public String getPassphrase() {
//			return null;
//		}
//
//		@Override
//		public String getPassword() {
//			return password;
//		}
//
//		@Override
//		public boolean promptPassword(String message) {
//			return true;
//		}
//
//		@Override
//		public boolean promptPassphrase(String message) {
//			return true;
//		}
//
//		@Override
//		public boolean promptYesNo(String message) {
//			return true;
//		}
//
//		@Override
//		public void showMessage(String message) {
//			
//		}
//		
//	}
	
	public SSHContext(){
		this(false);
	}
	
	public SSHContext(boolean isReuse){
		this.jsch= new JSch();
		this.isReuse = isReuse;
	}
	
	private Session getSession(){
		Session result;
		if(this.isReuse){
			result = this.reuseSession.get();
		} else {
			result = this.localSession;
		}
		return result;
	}
	
	private void setSession(Session session){
		if(this.isReuse){
			this.reuseSession.set(session);;
		} else {
			if(this.localSession != null && this.localSession.isConnected()){
				this.localSession.disconnect();
			}
			this.localSession = session;
			
		}
	}
	
	public void connectToHost(String host,int port,String account,String password,int timeout,Properties config,UserInfo userinfo) throws JSchException{
		Session session = this.jsch.getSession(account, host, port);
		session.setPassword(password);
		session.setConfig(config);
//		userinfo = new DefaultUserInfo(password);
		session.setUserInfo(userinfo);
		session.connect(timeout);
		this.setSession(session);
	}
	
	public void connectToHost(String host,int port,String account,String password,Properties config,int timeout) throws JSchException{
//        UserInfo userinfo = new DefaultUserInfo(password);
		this.connectToHost(host, port, account, password, timeout, config, null);
		
	}
	
	public void connectToHost(String host,int port,String account,String password,UserInfo userinfo,int timeout) throws JSchException{
		Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
		this.connectToHost(host, port, account, password, timeout, config, userinfo);
		
	}
	
	public void connectToHost(String host,int port,String account,String password,int timeout) throws JSchException{
		Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
//        UserInfo userinfo = new DefaultUserInfo(password);
		this.connectToHost(host, port, account, password, timeout, config, null);
		
	}
	
	public byte[] executeByExec(String command,int timeout,Map<String, String> env) throws IOException{
		Session session = this.getSession();
		if(session == null || !session.isConnected()){
			throw new SSHException("没有找到Session信息");
		}
		ChannelExec channel;
		try {
			channel = (ChannelExec)session.openChannel("exec");
		} catch (JSchException e) {
			throw new SSHException("创建exec通道失败:" + e.getMessage(),e);
		}
		channel.setInputStream(null);
		channel.setCommand(command);
		if(env != null){
			Set<Entry<String,String>> entrySet = env.entrySet();
			for (Entry<String, String> entry : entrySet) {
				channel.setEnv(entry.getKey(), entry.getValue());
			}
		}
		try {
			channel.connect(timeout);
		} catch (JSchException e) {
			throw new SSHException("链接channel失败:" + e.getMessage(),e);
		}
		InputStream in;
		try {
			in = channel.getInputStream();
		} catch (IOException e) {
			channel.disconnect();
			throw e;
		}
		byte[] result;
		try {
			result = StreamProcessor.readByte(in);
		} catch (IOException e) {
			throw e;
		}finally{
			try {
				in.close();
			} catch (IOException ie) {
			}
			channel.disconnect();
		}
 		return result;
	}
	
	public void executeByShell(SSHShellProcessor processor,int timeout,Map<String, String> env,Map<Object,Object> args) throws IOException{
		Session session =  this.getSession();
		if(session == null || !session.isConnected()){
			throw new SSHException("没有找到Session信息");
		}
		ChannelShell channel;
		try {
			channel = (ChannelShell)session.openChannel("shell");
		} catch (JSchException e) {
			throw new SSHException("创建shell通道失败:" + e.getMessage(),e);
		}
		if(env != null){
			Set<Entry<String,String>> entrySet = env.entrySet();
			for (Entry<String, String> entry : entrySet) {
				channel.setEnv(entry.getKey(), entry.getValue());
			}
		}
		InputStream in = channel.getInputStream();
		OutputStream out = null;
		try {
			out = channel.getOutputStream();
		} catch (IOException ie) {
			try {
				in.close();
			} catch (IOException e) {
			}
			throw ie;
		}
		try {
			channel.connect(timeout);
		} catch (JSchException e) {
			try {
				in.close();
			} catch (IOException e1) {
			}
			try {
				out.close();
			} catch (IOException e1) {
			}
			throw new SSHException("链接channel失败:" + e.getMessage(),e);
		}
		try {
			processor.process(in, out,args);
		} catch (IOException e) {
			throw new SSHException("JSCH错误->获取输入流错误" + e.getMessage(),e);
		} finally {
			try {
				in.close();
			} catch (IOException e1) {
			}
			try {
				out.close();
			} catch (IOException e1) {
			}
			channel.disconnect();
		}
	}
	
	public void executeBySFtp(SSHSFtpProcessor processor,int timeout,Map<String, String> env,Map<Object,Object> args) throws IOException{
		Session session = this.getSession();
		if(session == null || !session.isConnected()){
			throw new SSHException("没有找到Session信息");
		}
		ChannelSftp channel;
		try {
			channel = (ChannelSftp)session.openChannel("sftp");
		} catch (JSchException e) {
			throw new SSHException("创建shell通道失败:" + e.getMessage(),e);
		}
		if(env != null){
			Set<Entry<String,String>> entrySet = env.entrySet();
			for (Entry<String, String> entry : entrySet) {
				channel.setEnv(entry.getKey(), entry.getValue());
			}
		}
		try {
			channel.connect(timeout);
		} catch (JSchException e) {
			throw new SSHException("链接channel失败:" + e.getMessage(),e);
		}
		try {
			processor.process(channel,args);
		} catch (SSHException e) {
			throw e;
		} finally {
			channel.disconnect();
		}
	}
	
	public void close(){
		Session session = this.getSession();
		if(session != null && session.isConnected()){
			session.disconnect();
			this.localSession = null;
			this.reuseSession.set(null);
		}
	}	
	
	public static void main(String[] args) throws JSchException, IOException {
		SSHContext ctx = new SSHContext();
		ctx.connectToHost("192.168.1.105", 22, "root", "cctv", 0);
		byte[] res = ctx.executeByExec("osoperator dir size /bin", 0, null);
		ctx.close();
		System.out.println(res.length);
		System.out.println(new String(res));
	}
}
