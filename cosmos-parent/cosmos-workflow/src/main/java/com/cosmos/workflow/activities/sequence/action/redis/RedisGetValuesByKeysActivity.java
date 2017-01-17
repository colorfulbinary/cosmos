package com.cosmos.workflow.activities.sequence.action.redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisGetValuesByKeysActivity extends ActionActivity{

	private static final long serialVersionUID = -8882785136331839553L;
	
	private String client;
	
	private String keys;
	
	private KEYS_TYPE type;	
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public KEYS_TYPE getType() {
		return type;
	}

	public void setType(KEYS_TYPE type) {
		this.type = type;
	}

	
		
	@Override
	public void release() {
		this.client = null;
		this.type = null;
		this.keys = null;
	}
	
	@SuppressWarnings("unchecked")
	private Response<Object> getValue(Pipeline pipe,String key)  throws WorkflowException{
		Object result = null;
		switch (this.type) {
			case STRING:
				try {
					result = pipe.get(key);
				} catch (Exception e) {
					throw new WorkflowException(this,"获取VALUE错误",e);
				}
				break;
			case BINARY:
				try {
					result = pipe.get(key.getBytes());
				} catch (Exception e) {
					throw new WorkflowException(this,"获取VALUE错误",e);
				}
				break;
			case HASH_STRING:
				try {
					result = pipe.hgetAll(key);
				} catch (Exception e) {
					throw new WorkflowException(this,"获取VALUE错误",e);
				}
				break;
			case HASH_BINARY:
				try {
					result = pipe.hgetAll(key.getBytes());
				} catch (Exception e) {
					throw new WorkflowException(this,"获取VALUE错误",e);
				}
				break;
			default:
				throw new WorkflowException(this,"没有找到对应的类型");
		}
		return (Response<Object>) result;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object> exec(Jedis jedis,Object keys) throws WorkflowException{
		Map<String,Object> result = new HashMap<String, Object>();
		Pipeline pipe = jedis.pipelined();
		if(keys instanceof String[]){
			String[] ls =  (String[]) keys;
			for (String key : ls) {
				Response<Object> value = this.getValue(pipe, key);
				result.put(key, value);
			}
		} else if(keys instanceof Collection<?>){
			Collection<String> ls;
			try {
				ls = (Collection<String>) keys;
			} catch (Exception e) {
				throw new WorkflowException(this,"集合类型不匹配!必须为Collection<String>",e);
			}
			for (String key : ls) {
				Response<Object> value = this.getValue(pipe, key);
				result.put(key, value);
			}
		} else {
			throw new WorkflowException(this,"无法匹配Keys集合类型");
		}
		try {
			pipe.sync();
		} catch (Exception e) {
			throw new WorkflowException(this,"同步redis命令失败",e);
		}
		for (Map.Entry<String, Object> item : result.entrySet()) {
			String key = item.getKey();
			Response<Object> value = (Response<Object>) item.getValue();
			result.put(key, value.get());
		}
		return result;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object result = null;
		Object obj = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null){
			Object keys = data.get(this.keys);
			if(keys == null){
				throw new WorkflowException(this,"KEYS不能为null");
			}
			if(obj instanceof Jedis){
				Jedis jedis = (Jedis) obj;
				result = this.exec(jedis,keys);
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
