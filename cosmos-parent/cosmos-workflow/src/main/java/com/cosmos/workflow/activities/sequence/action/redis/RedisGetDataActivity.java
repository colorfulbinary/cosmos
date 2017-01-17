package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisGetDataActivity extends ActionActivity{

	private static final long serialVersionUID = 2856554850909014164L;
	
	private String client;
	
	private String key;
	
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
	
	@Override
	public void release() {
		this.client = null;
		this.key = null;
		this.type = null;
	}
	
	private Object exec(Jedis jedis,String keyVal) throws WorkflowException{
		Object result = null;
		try {
			if(this.type == STRING_TYPE.STRING){
				result = jedis.get(keyVal);
			} else {
				result = jedis.get(keyVal.getBytes());
			}
		} catch (Exception e) {
			throw new WorkflowException(this,"redis操作失败",e);
		}
		return result;
	}
	
	private Object exec(Pipeline pipe,String keyVal) throws WorkflowException{
		Object result = null;
		try {
			if(this.type == STRING_TYPE.STRING){
				result = pipe.get(keyVal);
			} else {
				result = pipe.get(keyVal.getBytes());
			}
		} catch (Exception e) {
			throw new WorkflowException(this,"redis操作失败",e);
		}
		return result;
	}
	

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object result = null;
		Object obj = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null){
			String keyVal = data.getBoundValue(this.key);
			if(StringUtils.isEmptyOrNull(keyVal)){
				throw new WorkflowException(this,"没有找到指定的KEY");
			}
			if(obj instanceof Jedis){
				Jedis jedis = (Jedis) obj;
				result = exec(jedis,keyVal);
			} else if(obj instanceof Pipeline){
				Pipeline pipe = (Pipeline) obj;
				result = exec(pipe,keyVal);
			} else {
				throw new WorkflowException(this,"client类型只能是JEDIS,PIPE");
			}
		} else {
			throw new WorkflowException(this,"没有找到client");
		}
		if(!StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, result);
		}
	}
}
