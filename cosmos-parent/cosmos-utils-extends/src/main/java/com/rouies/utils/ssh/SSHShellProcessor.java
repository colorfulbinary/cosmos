package com.rouies.utils.ssh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import com.cosmos.utils.io.StreamProcessor;

public abstract class SSHShellProcessor {
	
	private String charset;
	
	private byte[][] endChars;
	
	private int endCharMaxLength;
	
	public SSHShellProcessor(String charset,String[] endChars) throws SSHException{
		this.charset = charset;
		int endCharLength = endChars.length;
		this.endChars = new byte[endCharLength][];
		this.endCharMaxLength = 0;
		for (int i = 0; i < endCharLength; i++) {
			try {
				this.endChars[i] = endChars[i].getBytes(charset);
				int len = this.endChars[i].length;
				if(len > this.endCharMaxLength){
					this.endCharMaxLength = len;
				}
			} catch (UnsupportedEncodingException e) {
				throw new SSHException("无法解析字符集");
			}
		}
		
	}
	
	protected class ShellProcessor{
		
		private ShellProcessor(){};
		
		private InputStream in;
		
		private OutputStream out;
		
		private boolean checkEnd(byte[] source){
			boolean isOver = false;
			for (byte[] endChar : endChars) {
				if(source.length < endChar.length){
					continue;
				}
				byte[] endTag = new byte[endChar.length];
				System.arraycopy(source, source.length - endChar.length, endTag, 0, endChar.length);
				if(Arrays.equals(endTag, endChar)){
					isOver = true;
					break;
				}
			}
			return isOver;
		}
		
		private void readCommandResult(OutputStream result) throws Exception{
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] buffered = new byte[4096];
			int index = -1;
			while((index = this.in.read(buffered, 0, 4096)) > 0){
				out.write(buffered,0,index);
				System.out.println(new String(out.toByteArray(),"gbk"));
				if(checkEnd(out.toByteArray())){
					break;
				}
			}
			StreamProcessor.forword(new ByteArrayInputStream(out.toByteArray()), result);
		}
		
		public String executeCommand(String command) throws SSHException{
			ByteArrayOutputStream out = new ByteArrayOutputStream(512);
			this.executeCommand(command, out);
			String result;
			try {
				result = new String(out.toByteArray(),SSHShellProcessor.this.charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new SSHException("无法解析字符集");
			}
			return result.substring(result.indexOf('\n') + 1, result.lastIndexOf('\n') -1);
		}
		
		
		public void executeCommand(String command,OutputStream result) throws SSHException{
			command += "\r\n";
			byte[] commandBytes;
			try {
				commandBytes = command.getBytes(SSHShellProcessor.this.charset);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new SSHException("无法解析字符集:" + e.getMessage());
			}
			try {
				this.out.write(commandBytes);
				this.out.flush();
			} catch (IOException e) {
				e.printStackTrace();
				throw new SSHException("发送命令[" + command + "]失败:" + e.getMessage());
			}
			try {
				this.readCommandResult(result);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SSHException("解析命令[" + command + "]结果失败:" + e.getMessage());
			}
		}
	}
	
	void process(InputStream in,OutputStream out,Map<Object,Object> args) throws SSHException{
		ShellProcessor sp = new ShellProcessor();
		sp.in = in;
		sp.out = out;
		try {
			ByteArrayOutputStream nbf = new ByteArrayOutputStream(512);
			sp.readCommandResult(nbf);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SSHException("解析初始报文错误");
		}
		this.execute(sp,args);
	}
	
	public abstract void execute(ShellProcessor processor,Map<Object,Object> args)  throws SSHException;
}
