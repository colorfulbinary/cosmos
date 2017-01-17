package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisExistsActivity extends ActionActivity{
	
	private static final long serialVersionUID = -4561381401025810259L;

	private String client;
	
	private String key;
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void release() {
		this.client = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		boolean result;
		Object obj = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null && (obj instanceof Jedis)){
			@SuppressWarnings("resource")
			Jedis jedis = (Jedis) obj;
			String keyVal = data.getBoundValue(this.key);
			if(StringUtils.isEmptyOrNull(keyVal)){
				throw new WorkflowException(this,"没有找到指定的KEY");
			}
			try {
				result = jedis.exists(keyVal);
			} catch (Exception e) {
				throw new WorkflowException(this,"无法判断是否存在");
			}
		} else {
			throw new WorkflowException(this,"没有找到要操作的client");
		}
		if(!StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, result);
		}
	}
}