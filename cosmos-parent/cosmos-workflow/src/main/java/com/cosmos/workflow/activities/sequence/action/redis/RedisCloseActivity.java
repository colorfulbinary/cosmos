package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisCloseActivity extends ActionActivity{

	private static final long serialVersionUID = -740463125689008602L;
	
	private String client;
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	@Override
	public void release() {
		this.client = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object obj = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null && obj instanceof Jedis){
			Jedis jedis = (Jedis) obj;
			jedis.close();
		} else {
			throw new WorkflowException(this,"没有找到要关闭的client");
		}
	}

}
