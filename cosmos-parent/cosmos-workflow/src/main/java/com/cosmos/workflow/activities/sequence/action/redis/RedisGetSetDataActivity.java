package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisGetSetDataActivity extends ActionActivity{
	
	private static final long serialVersionUID = -1298487723153806829L;

	private String client;
	
	private String key;
	
	private String value;
	
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
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	@Override
	public void release() {
		this.client =  null;
		this.key = null;
		this.value = null;
		
	}
	
	private Object exec(Jedis jedis,String keyVal,Object val) throws WorkflowException{
		Object result = null;
		try {
			if(this.type == STRING_TYPE.STRING && val instanceof String){
				result = jedis.getSet(keyVal, val.toString());
			} else if(this.type == STRING_TYPE.BINARY && val instanceof byte[]){
				result = jedis.getSet(keyVal.getBytes(), (byte[])val);
			} else {
				throw new WorkflowException(this,"类型不匹配");
			}
		} catch (Exception e) {
			throw new WorkflowException(this,"设置Redis错误",e);
		}
		return result;
	}
	
	private Object exec(Pipeline pipe,String keyVal,Object val) throws WorkflowException{
		Object result = null;
		try {
			if(this.type == STRING_TYPE.STRING && val instanceof String){
				result = pipe.getSet(keyVal, val.toString());
			} else if(this.type == STRING_TYPE.BINARY && val instanceof byte[]){
				result = pipe.getSet(keyVal.getBytes(), (byte[])val);
			} else {
				throw new WorkflowException(this,"类型不匹配");
			}
		} catch (Exception e) {
			throw new WorkflowException(this,"设置Redis错误",e);
		}
		return result;
	}
	
	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object result = null;
		Object obj = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null){
			String keyVal = data.getBoundValue(this.key);
			Object val    = data.get(this.value);
			if(StringUtils.isEmptyOrNull(keyVal) || val == null){
				throw new WorkflowException(this,"KEY与VALUE不能为null");
			}
			if(obj instanceof Jedis){
				Jedis jedis = (Jedis) obj;
				result = exec(jedis,keyVal,val);
			} else if(obj instanceof Pipeline){
				Pipeline pipe = (Pipeline) obj;
				result = exec(pipe,keyVal,val);
			} else {
				throw new WorkflowException(this,"client类型只能是JEDIS,PIPE");
			}
			
		} else {
			throw new WorkflowException(this,"没有找到要操作的client");
		}
		if(!StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, result);
		}
	}
	
}
