package com.cosmos.workflow.activities.sequence.action.redis;

import java.util.Set;

import redis.clients.jedis.Jedis;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisKeysActivity extends ActionActivity{

	private static final long serialVersionUID = -4023032923127664385L;
	
	private String client;
	
	private String pattern;
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void release() {
		this.client = null;
		this.pattern = null;
	}

	@SuppressWarnings("resource")
	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object result = null;
		Object obj = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null){
			String pattern = data.getBoundValue(this.pattern);
			if(StringUtils.isEmptyOrNull(pattern)){
				throw new WorkflowException(this,"没有找到指定的pattern");
			}
			if(obj instanceof Jedis){
				Jedis jedis = (Jedis) obj;
				Set<String> keys;
				try {
					keys = jedis.keys(pattern);
				} catch (Exception e) {
					throw new WorkflowException(this,"查询KEYS错误", e);
				}
				result = keys.toArray(new String[0]);
			} else {
				throw new WorkflowException(this,"client类型只能是JEDIS");
			}
		}
		if(!StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, result);
		}
	}

}
