package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.rouies.utils.redis.RedisContext;
import com.rouies.utils.redis.RedisException;

public class RedisClientActivity extends ActionActivity{
	
	private static final long serialVersionUID = 1928358034183614082L;

	private String host;
	
	private String port;
	
	private String password;
		
	private int timeout = 0;
	
	private int index;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public void release() {
		this.host = null;
		this.password = null;
		this.port = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Jedis result = null;
		String host      = data.getBoundValue(this.host);
		String port      = data.getBoundValue(this.port);
		String password = data.getBoundValue(this.password);
		if(StringUtils.isEmptyOrNull(host) 
				|| StringUtils.isEmptyOrNull(port)
				|| StringUtils.isEmptyOrNull(password)){
			throw new WorkflowException(this,"配置信息错误");
		}
		int portVal = 0;
		int timeoutVal = 0;
		try {
			portVal = Integer.valueOf(port);
			timeoutVal = Integer.valueOf(this.timeout);
		} catch (NumberFormatException e) {
			throw new WorkflowException(this,"无法解析端口!",e);
		}
		try {
			result = RedisContext.getClient(host, portVal, password, timeoutVal);
			result.select(this.index);
		} catch (RedisException e) {
			throw new WorkflowException(this,"获取客户端失败!",e);
		}
		data.put(this.out, result);
	}

}
