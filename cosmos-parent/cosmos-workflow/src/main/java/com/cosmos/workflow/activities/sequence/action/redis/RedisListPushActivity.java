package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisListPushActivity extends ActionActivity{
	
	private static final long serialVersionUID = 525845690996254850L;

	private String client;
	
	private String key;
	
	private String value;
	
	private LIST_DIRECTION direction;
	
	private NX_TYPE nx;
	
	private STRING_TYPE type;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

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

	public NX_TYPE getNx() {
		return nx;
	}

	public void setNx(NX_TYPE nx) {
		this.nx = nx;
	}

	@Override
	public void release() {
		this.client = null;
		this.direction = null;
		this.key = null;
		this.nx = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object result = null;
		Object obj = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null && obj instanceof Jedis){
			String keyVal = data.getBoundValue(this.key);
			Object val    = data.get(this.value);
			if(StringUtils.isEmptyOrNull(keyVal) || val == null){
				throw new WorkflowException(this,"KEY与VALUE不能为null");
			}
			Jedis jedis = (Jedis) obj;
			try {
				result = this.direction.push(jedis, this.nx, this.type, keyVal, val);
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
