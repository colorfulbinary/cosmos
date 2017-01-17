package com.cosmos.workflow.activities.sequence.action.redis;

import java.util.Map;

import com.cosmos.workflow.activities.WorkflowException;

import redis.clients.jedis.Jedis;

public interface IRedisOperator {
	public void execute(Jedis jedis,Map<Object, Object> payload) throws WorkflowException;
}
