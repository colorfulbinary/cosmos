package com.cosmos.workflow.activities.sequence.action.network.ssh;

import java.io.IOException;
import java.util.Map;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.rouies.utils.ssh.SSHContext;

public class SSHExecActivity extends ActionActivity{
	
	private static final long serialVersionUID = 5939931233768464294L;

	private String client;
	
	private String charset;
	
	private String command;
	
	private String[] arguments;
	
	private int timeout;
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Map<String, String> getEnv() {
		return env;
	}

	public void setEnv(Map<String, String> env) {
		this.env = env;
	}

	private Map<String, String> env;
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	@Override
	public void release() {
		this.charset = null;
		this.client = null;
		this.command = null;
		this.arguments = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object obj = data.get(this.client);
		if(obj!= null && obj instanceof SSHContext){
			SSHContext ctx = (SSHContext) obj;
			Object[] args = new String[this.arguments.length];
			for (int i = 0,len = this.arguments.length; i < len; i++) {
				Object ab = data.get(this.arguments[i]);
				String arg = null;
				if(ab != null && !(arg = ab.toString().trim()).equals("")){
					args[i] =  arg;
				} else {
					throw new WorkflowException(this, "命令参数不能为空");
				}
			}
			String cmd = String.format(this.command, args);
			try {
				byte[] result = ctx.executeByExec(cmd, this.timeout, this.env);
				data.put(this.out, new String(result,this.charset));
			} catch (IOException e) {
				throw new WorkflowException(this,"执行命令发生错误:" + e.getMessage(),e);
			}
		} else {
			throw new WorkflowException(this,"没有找到client");
		}
	}

}
