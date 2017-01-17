package com.cosmos.workflow.activities.sequence.action.network.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.rouies.utils.ssh.SSHContext;
import com.rouies.utils.ssh.SSHException;
import com.rouies.utils.ssh.SSHSFtpProcessor;
import com.rouies.utils.ssh.SSHShellProcessor;

public class SSHSFtpActivity extends ActionActivity{
	
	private static final long serialVersionUID = 6378755462866065595L;

	public static final int DELETE_FILE = 0;
	
	public static final int DELETE_DIRECTORY = 1;
	
	public static final int MAKE_DIRECTORY = 2;
	
	public static final int UPLOAD_FILE = 3;
	
	public static final int DOWNLOAD_FILE = 4;
	
	public static final int CH_OWNER = 5;
	
	public static final int CH_GRP = 6;
	
	public static final int CH_MOD = 7;
	
	private class Command{
		
		public int getCommandType() {
			return commandType;
		}

		public void setCommandType(int commandType) {
			this.commandType = commandType;
		}

		public Object[] getArguments() {
			return arguments;
		}

		public void setArguments(Object[] arguments) {
			this.arguments = arguments;
		}

		private int commandType;
		
		private Object[] arguments;
	}
	
	private class WorkflowSFtpProcessor extends SSHSFtpProcessor{
		
		private Command[] commands;
		
		private WorkflowSFtpProcessor(Command[] commands){
			this.commands = commands;
		}

		@Override
		public void execute(SFtpProcessor processor, Map<Object, Object> args)
				throws Exception {
			for (int i = 0,len = this.commands.length; i < len; i++) {
				Command command = this.commands[i];
				switch (command.getCommandType()) {
				case SSHSFtpActivity.DELETE_FILE:
					this.deleteFile(command, processor, args);
					break;
				case SSHSFtpActivity.DELETE_DIRECTORY:
					this.deleteDirectory(command, processor, args);
					break;
				case SSHSFtpActivity.MAKE_DIRECTORY:
					this.makeDirectory(command, processor, args);
					break;
				case SSHSFtpActivity.UPLOAD_FILE:
					this.uploadFile(command, processor, args);
					break;
				case SSHSFtpActivity.DOWNLOAD_FILE:
					this.downloadFile(command, processor, args);
					break;
				case SSHSFtpActivity.CH_OWNER:
					this.chown(command, processor, args);
					break;
				case SSHSFtpActivity.CH_GRP:
					this.chgrp(command, processor, args);
					break;
				case SSHSFtpActivity.CH_MOD:
					this.chmod(command, processor, args);
					break;
				default:
					break;
				}
			}
		}
		
		private String getArgs(Map<Object, Object> args,String pars){
			String result = null;
			if(pars.startsWith("{")){
				Object target = args.get(pars.substring(1,pars.length() - 1));
				if(target!=null &&  !target.toString().trim().equals("")){
					result = target.toString();
				}	
			} else {
				result = pars;
			}
			return result;
		}
		
		private void deleteFile(Command command,SFtpProcessor processor, Map<Object, Object> args) throws Exception{
			Object[] commandArgs = command.getArguments();
			if(commandArgs.length > 0 && commandArgs[0] != null){
				String path = commandArgs[0].toString();
				path = this.getArgs(args, path);
				processor.deleteFile(path);
			} else {
				throw new SSHException("必须指定一个参数");
			}
		}
		
		private void deleteDirectory(Command command,SFtpProcessor processor, Map<Object, Object> args) throws Exception{
			Object[] commandArgs = command.getArguments();
			if(commandArgs.length > 0 && commandArgs[0] != null){
				String path = commandArgs[0].toString();
				path = this.getArgs(args, path);
				processor.deleteDirectory(path);
			} else {
				throw new SSHException("必须指定一个参数");
			}
		}
		
		private void makeDirectory(Command command,SFtpProcessor processor, Map<Object, Object> args) throws Exception{
			Object[] commandArgs = command.getArguments();
			if(commandArgs.length > 0 && commandArgs[0] != null){
				String path = commandArgs[0].toString();
				path = this.getArgs(args, path);
				processor.makeDirectory(path);
			} else {
				throw new SSHException("必须指定一个参数");
			}
		}
		
		private void uploadFile(Command command,SFtpProcessor processor, Map<Object, Object> args) throws Exception{
			Object[] commandArgs = command.getArguments();
			if(commandArgs.length == 2 && commandArgs[0] != null && commandArgs[1] != null){
				Object in = args.get(commandArgs[0].toString());
				InputStream input = null;
				if(in != null && in instanceof InputStream){
					input = (InputStream) in;
				}
				String path = commandArgs[1].toString();
				path = this.getArgs(args, path);
				processor.uploadFile(input, path, SFtpProcessor.OVERWRITE);
			} else {
				throw new SSHException("必须指定参数");
			}
		}
		
		private void downloadFile(Command command,SFtpProcessor processor, Map<Object, Object> args) throws Exception{
			Object[] commandArgs = command.getArguments();
			if(commandArgs.length == 2 && commandArgs[0] != null && commandArgs[1] != null){
				String path = commandArgs[0].toString();
				path = this.getArgs(args, path);
				Object out = args.get(commandArgs[1].toString());
				OutputStream output = null;
				if(out != null && out instanceof OutputStream){
					output = (OutputStream) out;
				}
				processor.downloadFile(path, output);
			} else {
				throw new SSHException("必须指定参数");
			}
		}
		
		private void chown(Command command,SFtpProcessor processor, Map<Object, Object> args) throws Exception{
			Object[] commandArgs = command.getArguments();
			if(commandArgs.length == 2 && commandArgs[0] != null && commandArgs[1] != null){
				String uid  = commandArgs[0].toString();
				String path = commandArgs[1].toString();
				uid = this.getArgs(args, uid);
				path = this.getArgs(args, path);
				processor.chown(new Integer(uid), path);
			} else {
				throw new SSHException("必须指定参数");
			}
		}
		
		private void chgrp(Command command,SFtpProcessor processor, Map<Object, Object> args) throws Exception{
			Object[] commandArgs = command.getArguments();
			if(commandArgs.length == 2 && commandArgs[0] != null && commandArgs[1] != null){
				String gid  = commandArgs[0].toString();
				String path = commandArgs[1].toString();
				gid = this.getArgs(args, gid);
				path = this.getArgs(args, path);
				processor.chgrp(new Integer(gid), path);
			} else {
				throw new SSHException("必须指定参数");
			}
		}
		
		private void chmod(Command command,SFtpProcessor processor, Map<Object, Object> args) throws Exception{
			Object[] commandArgs = command.getArguments();
			if(commandArgs.length == 2 && commandArgs[0] != null && commandArgs[1] != null){
				String permissions  = commandArgs[0].toString();
				String path = commandArgs[1].toString();
				path = this.getArgs(args, path);
				char[] items = permissions.toCharArray();
				if(items.length != 3){
					throw new SSHException("permissions语法错误");
				}
				int permission = 0;
				//owner
				int ownerPermission = new Integer(String.valueOf(items[0]));
				if((ownerPermission & 1) == 1){
					permission |= SFtpPermission.OWNER_EXECUTE;
				}
				if((ownerPermission & 2) == 2){
					permission |= SFtpPermission.OWNER_WRITE;
				}
				if((ownerPermission & 4) == 4){
					permission |= SFtpPermission.OWNER_READ;
				}
				//group
				int groupPermission = new Integer(String.valueOf(items[1]));
				if((groupPermission & 1) == 1){
					permission |= SFtpPermission.GROUP_EXECUTE;
				}
				if((groupPermission & 2) == 2){
					permission |= SFtpPermission.GROUP_WRITE;
				}
				if((groupPermission & 4) == 4){
					permission |= SFtpPermission.GROUP_READ;
				}
				//other
				int otherPermission = new Integer(String.valueOf(items[2]));
				if((otherPermission & 1) == 1){
					permission |= SFtpPermission.OTHER_EXECUTE;
				}
				if((otherPermission & 2) == 2){
					permission |= SFtpPermission.OTHER_WRITE;
				}
				if((otherPermission & 4) == 4){
					permission |= SFtpPermission.OTHER_READ;
				}
				processor.chmod(permission, path);
			} else {
				throw new SSHException("必须指定参数");
			}
		}
	}
	
	private List<Command> commands = new ArrayList<SSHSFtpActivity.Command>(5);
	
	public void appendCommand(int commandType,Object...args){
		Command command = new Command();
		command.setCommandType(commandType);
		command.setArguments(args);
		commands.add(command);
	}
	
	private int timeout;
	
	private String client;
	
	private String processor;
	
	private HashMap<String, String> env;

	public HashMap<String, String> getEnv() {
		return env;
	}

	public void setEnv(HashMap<String, String> env) {
		this.env = env;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	@Override
	public void release() {
		this.client = null;
		this.processor = null;
	}
	

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object obj = data.get(this.client);
		SSHContext ctx = null;
		if(obj!= null && obj instanceof SSHContext){
			ctx = (SSHContext) obj;
		} else {
			throw new WorkflowException(this,"没有找到client");
		}
		SSHSFtpProcessor processor = null;
		if(!StringUtils.isEmptyOrNull(this.processor) && (obj = data.get(this.processor))!=null 
				&& obj instanceof SSHShellProcessor){
			processor = (SSHSFtpProcessor) obj; 
		} else {
			if(this.commands.size() == 0){
				throw new WorkflowException(this, "至少有一个要执行的命令");
			}
			try {
				processor = new WorkflowSFtpProcessor(this.commands.toArray(new Command[0]));
			} catch (Exception e) {
				throw new WorkflowException(this, "无法解析处理器字符集", e);
			}
		}
		try {
			ctx.executeBySFtp(processor, this.timeout, this.env, data);
		} catch (IOException e) {
			throw new WorkflowException(this, "ssh-sftp执行错误:" + e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(String.valueOf('a'));
	}

}
