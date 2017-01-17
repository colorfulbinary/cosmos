package com.cosmos.workflow.activities.sequence.action.network.ssh;

import java.io.IOException;
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
import com.rouies.utils.ssh.SSHShellProcessor;

public class SSHShellActivity extends ActionActivity{
	
	private static final long serialVersionUID = 4410611692423495682L;

	private class Command {
		
		private String command;
		
		private String[] arguments;
		
		private String out;

		public String getOut() {
			return out;
		}

		public String getCommand() {
			return command;
		}

		public String[] getArguments() {
			return arguments;
		}
		
		private Command(String command,String out,String...args){
			this.command = command;
			this.arguments = args;
			this.out = out;
		}
		
	}
	
	private class WorkflowShellProcessor extends SSHShellProcessor{
		
		private Command[] command;
		
		private WorkflowShellProcessor(String charset,String[] endChars,Command[] command) throws SSHException{
			super(charset,endChars);
			this.command = command;
		}
		
		private Object[] parseArguments(String[] arguments, Map<Object, Object> data) throws SSHException{
			Object[] result = new Object[arguments.length];
			for (int i = 0,len = result.length; i < len; i++) {
				Object ab = data.get(arguments[i]);
				String arg = null;
				if(ab != null && !(arg = ab.toString().trim()).equals("")){
					result[i] =  arg;
				} else {
					throw new SSHException("命令参数不能为空");
				}
			}
			return result;
		}
		@Override
		public void execute(ShellProcessor processor, Map<Object, Object> data)
				throws SSHException {
			for (int i = 0,len = this.command.length; i < len; i++) {
				String command = this.command[i].getCommand();
				String[] arguments = this.command[i].getArguments();
				String out = this.command[i].getOut();
				Object[] args = this.parseArguments(arguments, data);
				command = String.format(command, args);
				String result = processor.executeCommand(command);
				data.put(out, result);
			}
		}
	}
	
	private List<Command> commands = new ArrayList<SSHShellActivity.Command>(5);
	
	public void appendCommnad(String command,String out,String...args){
		commands.add(new Command(command, out,args));
	}
	
	private String charset;
	
	private String[] endChars;
	
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

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String[] getEndChars() {
		return endChars;
	}

	public void setEndChars(String[] endChars) {
		this.endChars = endChars;
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
		this.charset = null;
		this.client = null;
		this.commands.clear();
		this.commands = null;
		this.endChars = null;
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
		SSHShellProcessor processor = null;
		if(!StringUtils.isEmptyOrNull(this.processor) && (obj = data.get(this.processor))!=null 
				&& obj instanceof SSHShellProcessor){
			processor = (SSHShellProcessor) obj; 
		} else {
			if(this.commands.size() == 0){
				throw new WorkflowException(this, "至少有一个要执行的命令");
			}
			try {
				processor = new WorkflowShellProcessor(this.charset,this.endChars,this.commands.toArray(new Command[0]));
			} catch (SSHException e) {
				throw new WorkflowException(this, "无法解析处理器字符集", e);
			}
		}
		try {
			ctx.executeByShell(processor, this.timeout, this.env, data);
		} catch (IOException e) {
			throw new WorkflowException(this, "ssh-shell执行错误:" + e.getMessage(), e);
		}
	}

}
