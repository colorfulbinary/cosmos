package com.cosmos.workflow.test;

import java.util.Map;

import redis.clients.jedis.Jedis;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.redis.IRedisOperator;

public class RedisShellTest implements IRedisOperator{

	@Override
	public void execute(Jedis jedis, Map<Object, Object> payload)
			throws WorkflowException {
		String res = jedis.get("name");
		payload.put("res", res);
	}

}
