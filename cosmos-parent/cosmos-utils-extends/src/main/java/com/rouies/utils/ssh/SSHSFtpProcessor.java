package com.rouies.utils.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.cosmos.utils.io.StreamProcessor;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

public abstract class SSHSFtpProcessor {
	
	private ChannelSftp sftp;
	
	protected interface SFtpPermission{
		public static final int OWNER_READ      = 00400;
		public static final int GROUP_READ      = 00040;
		public static final int OTHER_READ      = 00004;
		public static final int OWNER_WRITE     = 00200;
		public static final int GROUP_WRITE     = 00020;
		public static final int OTHER_WRITE     = 00002;
		public static final int OWNER_EXECUTE   = 00100;
		public static final int GROUP_EXECUTE   = 00010;
		public static final int OTHER_EXECUTE   = 00001;
		public static final int OWNER_ONLY_READ = 00500;
		public static final int GROUP_ONLY_READ = 00050;
		public static final int OTHER_ONLY_READ = 00005;
		public static final int OWNER_FULL      = 00700;
		public static final int GROUP_FULL      = 00070;
		public static final int OTHER_FULL      = 00007;
	}
	
	protected class SFtpProcessor{
		
		public static final int OVERWRITE = ChannelSftp.OVERWRITE;
		
		public static final int RESUME = ChannelSftp.RESUME;
		
		public static final int APPEND = ChannelSftp.APPEND;
				
		private SFtpProcessor(ChannelSftp channel){
			this.client = channel;
		};
		
		private ChannelSftp client = null;
		
		public void uploadFile(InputStream src,String target,int mode) throws SftpException{
			client.put(src,target, mode);
		}
		
		public void uploadFile(InputStream src,String target,SftpProgressMonitor progress,int mode) throws SftpException{
			client.put(src,target, progress, mode);
		}
		
		public void uploadFile(InputStream src,String target,SftpProgressMonitor progress,int mode,long offset) throws SftpException{
			OutputStream out = client.put(target, progress, mode, offset);
			try {
				StreamProcessor.forword(src, out);
			} catch (IOException e) {
				throw new SftpException(0, e.getMessage(), e);
			}
		}
		
		public void downloadFile(String src,OutputStream target) throws SftpException{
			client.get(src, target);
		}
		
		public void downloadFile(String src,OutputStream target,SftpProgressMonitor progress) throws SftpException{
			client.get(src, target, progress);
		}
		
		public void downloadFile(String src,OutputStream target,SftpProgressMonitor progress,int mode,long skip) throws SftpException{
			client.get(src, target, progress, mode, skip);
		}
		
		public void deleteFile(String path) throws SftpException{
			client.rm(path);
		}
		
		public void deleteDirectory(String path) throws SftpException{
			client.rmdir(path);
		}
		
		public void makeDirectory(String path) throws SftpException{
			client.mkdir(path);
		}
		
		public void chmod(int permissions, String path) throws SftpException{
			client.chmod(permissions, path);
		}
		
		public void chown(int uid,String path) throws SftpException{
			client.chown(uid, path);
		}
		
		public void chgrp(int gid, String path) throws SftpException{
			client.chgrp(gid,path);
		}
	}
	
	void process(ChannelSftp sftp,Map<Object,Object> args) throws SSHException{
		this.sftp = sftp;
		SFtpProcessor processor = new SFtpProcessor(this.sftp);
		try {
			this.execute(processor,args);
		} catch (Exception e) {
			throw new SSHException("执行SFTP错误:" + e.getMessage(),e);
		}
	}
	
	public abstract void execute(SFtpProcessor processor,Map<Object,Object> args) throws Exception;
	
}
