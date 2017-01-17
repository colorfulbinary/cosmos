package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisShellActivity extends ActionActivity{
	
	private static final long serialVersionUID = 7434557339657581151L;
	
	private String shell;
	
	private String client;
	
	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
	}

	@Override
	public void release() {
		this.shell = null;
	}

	@SuppressWarnings("resource")
	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object shl = null;
		Object obj = null;
		Jedis jedis = null;
		if((obj = data.get(this.client)) != null && obj instanceof Jedis){
			jedis = (Jedis) obj;
			if((shl = data.get(this.shell))!= null && shl instanceof IRedisOperator){
				IRedisOperator op = (IRedisOperator) shl;
				op.execute(jedis, data);
			} else {
				throw new WorkflowException(this,"没有找到要操作Shell对象");
			}
		} else {
			throw new WorkflowException(this,"没有找到要操作的client");
		}
		
	}

}
