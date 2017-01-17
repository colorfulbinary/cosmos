package com.cosmos.workflow.activities.sequence.action.redis;

import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisSetHashAllDataActivity  extends ActionActivity{
	
	
	private static final long serialVersionUID = 5066828791779543198L;
	
	private String client;
	
	private String key;
	
	private String value;
	
	private STRING_TYPE type;
	
	private long time = 0;
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
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
	
	public STRING_TYPE getType() {
		return type;
	}

	public void setType(STRING_TYPE type) {
		this.type = type;
	}

	@Override
	public void release() {
		this.client = null;
		this.key = null;
		this.value = null;
	}
	
	
	@SuppressWarnings("unchecked")
	private Object exec(Jedis jedis,String key,Object val) throws WorkflowException{
		Object result = null;
		try {
			if(this.type == STRING_TYPE.STRING){
				Map<String, String> dt;
				try {
					dt = (Map<String, String>) val;
				} catch (Exception e) {
					throw new WorkflowException(this,"无法将value转换为Map<String, String>",e);
				}
				result = jedis.hmset(key, dt);
			} else {
				Map<byte[], byte[]> dt;
				try {
					dt = (Map<byte[], byte[]>) val;
				} catch (Exception e) {
					throw new WorkflowException(this,"无法将value转换为Map<String, String>",e);
				}
				result = jedis.hmset(key.getBytes(), dt);
			}
		} catch (Exception e) {
			throw new WorkflowException(this,"设置Redis错误",e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Response<String> exec(Pipeline pipe,String keyVal,Object val) throws WorkflowException{
		Response<String> result = null;
		try {
			if(this.type == STRING_TYPE.STRING){
				Map<String, String> dt;
				try {
					dt = (Map<String, String>) val;
				} catch (Exception e) {
					throw new WorkflowException(this,"无法将value转换为Map<String, String>",e);
				}
				result = pipe.hmset(key, dt);
			} else {
				Map<byte[], byte[]> dt;
				try {
					dt = (Map<byte[], byte[]>) val;
				} catch (Exception e) {
					throw new WorkflowException(this,"无法将value转换为Map<String, String>",e);
				}
				result = pipe.hmset(key.getBytes(), dt);
			}
		} catch (Exception e) {
			throw new WorkflowException(this,"设置Redis错误",e);
		}
		return result;
	}
	

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object obj = null;
		Object result = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null){
			String keyVal = data.getBoundValue(this.key);
			Object value = data.get(this.value);
			if(StringUtils.isEmptyOrNull(keyVal) || value == null || !(value instanceof Map)){
				throw new WorkflowException(this,"key与value不能为null且value必须为map");
			}
			if(obj instanceof Jedis){
				Jedis jedis = (Jedis) obj;
				result = exec(jedis,keyVal,value);
				if(this.time > 0){
					jedis.pexpire(keyVal, this.time);
				}
			} else if(obj instanceof Pipeline){
				Pipeline pipe = (Pipeline) obj;
				result = exec(pipe,keyVal,value);
				if(this.time > 0){
					pipe.pexpire(keyVal, this.time);
				}
			} else {
				throw new WorkflowException(this,"client类型只能是JEDIS,PIPE");
			}
			
			if(!StringUtils.isEmptyOrNull(this.out)){
				data.put(this.out, result);
			}
		} else {
			throw new WorkflowException(this,"没有找到要操作的client");
		}
	}
}

