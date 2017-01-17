package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.rouies.utils.redis.RedisContext;
import com.rouies.utils.redis.RedisException;

public class RedisPoolClientActivity extends ActionActivity{
	
	private static final long serialVersionUID = 1928358034183614082L;
	
	private String poolName;
	
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	@Override
	public void release() {
		this.poolName =  null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Jedis result = null;
		String plName   = data.getBoundValue(this.poolName);
		if(StringUtils.isEmptyOrNull(plName)){
			throw new WorkflowException(this,"配置信息错误");
		}
		try {
			result = RedisContext.getClient(plName);
			result.select(this.index);
		} catch (RedisException e) {
			throw new WorkflowException(this,"获取客户端失败!",e);
		}
		data.put(this.out, result);
	}

}
