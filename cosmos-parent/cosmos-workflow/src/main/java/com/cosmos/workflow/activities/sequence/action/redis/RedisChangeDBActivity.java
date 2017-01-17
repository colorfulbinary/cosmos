package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisChangeDBActivity extends ActionActivity{
	
	private static final long serialVersionUID = -3210347788344265803L;

	private String client;
	
	private int index;
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void release() {
		this.client = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object obj = null;
		if(!StringUtils.isEmptyOrNull(this.client) && (obj = data.get(this.client)) != null && (obj instanceof Jedis)){
			@SuppressWarnings("resource")
			Jedis jedis = (Jedis) obj;
			try {
				jedis.select(index);
			} catch (Exception e) {
				throw new WorkflowException(this,"切换数据库失败");
			}
		} else {
			throw new WorkflowException(this,"没有找到要操作的client");
		}
	}
}
