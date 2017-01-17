package com.cosmos.workflow.activities.sequence.action.network.ssh;

import java.util.Properties;

import com.cosmos.utils.pools.SingletonObjectPools;
import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import com.rouies.utils.ssh.SSHContext;

public class SSHClientActivity extends ActionActivity{
	
	private static final long serialVersionUID = -7440682269430122305L;

	private String host;
	
	private String port;
	
	private String account;
	
	private String password;
	
	private Properties config;
	
	private String userInfo;
	
	private int timeout;
	
	private boolean isReuse;
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getConfig() {
		return config;
	}

	public void setConfig(Properties config) {
		this.config = config;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isReuse() {
		return isReuse;
	}

	public void setReuse(boolean isReuse) {
		this.isReuse = isReuse;
	}

	@Override
	public void release() {
		this.host = null;
		this.account  = null;
		this.password  = null;
		this.userInfo  = null;
		if(this.config != null){
			config.clear();
			this.config  = null;
		}
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		SSHContext ctx;
		if(this.isReuse){
			if(!SingletonObjectPools.exists(SSHContext.class)){
				ctx = new SSHContext(this.isReuse);
				SingletonObjectPools.registerSingletonObject(ctx);
			} else {
				ctx = SingletonObjectPools.getSingletonObject(SSHContext.class);
			}
		} else {
			ctx = new SSHContext(this.isReuse);
		}
		String host = data.getBoundValue(this.host);
		String port = data.getBoundValue(this.port);
		String account = data.getBoundValue(this.account);
		String password = data.getBoundValue(this.password);
		Object userInfo = null;
		if(!StringUtils.isEmptyOrNull(this.userInfo)){
			userInfo = data.get(this.userInfo);
		}
		if(!StringUtils.isEmptyOrNull(port) &&
			!StringUtils.isEmptyOrNull(host) &&
			!StringUtils.isEmptyOrNull(password) &&
			!StringUtils.isEmptyOrNull(account)){
			int portNum = new Integer(port.toString());
			try {
				if(userInfo == null && (this.config == null || this.config.size() ==0)){
					ctx.connectToHost(host, portNum, account, password, this.timeout);
				} else if(userInfo == null && this.config!=null && this.config.size() !=0){
					ctx.connectToHost(host, portNum, account, password, config, this.timeout);
				} else if(userInfo !=null && (this.config == null || this.config.size() ==0)){
					if(userInfo instanceof UserInfo){
						ctx.connectToHost(host, portNum, account, password, (UserInfo)userInfo, this.timeout);
					} else {
						throw new WorkflowException(this,"UserInfo类型不匹配");
					}
				} else {
					if(userInfo instanceof UserInfo){
						ctx.connectToHost(host, portNum, account, password, this.timeout, config, (UserInfo)userInfo);
					} else {
						throw new WorkflowException(this,"UserInfo类型不匹配");
					}
				}
				data.put(this.out, ctx);
			} catch (JSchException e) {
				throw new WorkflowException(this,"无法链接目标主机",e);
			}
		}
		
	}
	
}
