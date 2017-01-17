package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisListPopActivity extends ActionActivity{

	private static final long serialVersionUID = 8852142421403069270L;
	
	private String client;
	
	private String key;
	
	private LIST_DIRECTION direction;
	
	private STRING_TYPE type;
	
	public STRING_TYPE getType() {
		return type;
	}

	public void setType(STRING_TYPE type) {
		this.type = type;
	}

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

	public LIST_DIRECTION getDirection() {
		return direction;
	}

	public void setDirection(LIST_DIRECTION direction) {
		this.direction = direction;
	}

	@Override
	public void release() {
		this.client = null;
		this.direction = null;
		this.key = null;
	}
	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object obj = null;
		Object result = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null && obj instanceof Jedis){
			String keyVal = data.getBoundValue(this.key);
			if(StringUtils.isEmptyOrNull(keyVal) ){
				throw new WorkflowException(this,"KEY不能为null");
			}
			Jedis jedis = (Jedis) obj;
			try {
				result = this.direction.pop(jedis, this.type, keyVal);
			} catch (Exception e) {
				throw new WorkflowException(this,"Redis执行失败",e);
			}
		} else {
			throw new WorkflowException(this,"没有可用的client");
		}
		if(!StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, result);
		}
	}

}
