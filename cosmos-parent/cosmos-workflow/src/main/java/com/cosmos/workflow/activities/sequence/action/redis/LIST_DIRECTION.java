package com.cosmos.workflow.activities.sequence.action.redis;

import com.rouies.utils.redis.RedisException;

import redis.clients.jedis.Jedis;

public enum LIST_DIRECTION {
	LEFT(0),RIGHT(1);
	
	private int direction;
	
	LIST_DIRECTION(int direction){
		this.direction = direction;
	}
	
	public long push(Jedis jedis,NX_TYPE nx,STRING_TYPE type,String key,Object value) throws RedisException{
		long result = 0;
		if(this.direction == 0){
			if(nx == NX_TYPE.NX){
				if(type == STRING_TYPE.STRING && value instanceof String[]){
					String[] val = (String[]) value;
					result = jedis.lpushx(key, val);
				} else if(type == STRING_TYPE.STRING && value instanceof String){
					result = jedis.lpushx(key, value.toString());
				} else if(type == STRING_TYPE.BINARY && value instanceof byte[][]){
					byte[][] val = (byte[][]) value;
					result = jedis.lpushx(key.getBytes(), val);
				} else if(type == STRING_TYPE.BINARY && value instanceof byte[]){
					byte[] val = (byte[]) value;
					result = jedis.lpushx(key.getBytes(), val);
				} else {
					throw new RedisException("没有找到指定的类型");
				}
			} else {
				if(type == STRING_TYPE.STRING && value instanceof String[]){
					String[] val = (String[]) value;
					result = jedis.lpush(key, val);
				} else if(type == STRING_TYPE.STRING && value instanceof String){
					result = jedis.lpush(key, value.toString());
				} else if(type == STRING_TYPE.BINARY && value instanceof byte[][]){
					byte[][] val = (byte[][]) value;
					result = jedis.lpush(key.getBytes(), val);
				} else if(type == STRING_TYPE.BINARY && value instanceof byte[]){
					byte[] val = (byte[]) value;
					result = jedis.lpush(key.getBytes(), val);
				} else {
					throw new RedisException("没有找到指定的类型");
				}
			}
		} else {
			if(nx == NX_TYPE.NX){
				if(type == STRING_TYPE.STRING && value instanceof String[]){
					String[] val = (String[]) value;
					result = jedis.rpushx(key, val);
				} else if(type == STRING_TYPE.STRING && value instanceof String){
					result = jedis.rpushx(key, value.toString());
				} else if(type == STRING_TYPE.BINARY && value instanceof byte[][]){
					byte[][] val = (byte[][]) value;
					result = jedis.rpushx(key.getBytes(), val);
				} else if(type == STRING_TYPE.BINARY && value instanceof byte[]){
					byte[] val = (byte[]) value;
					result = jedis.rpushx(key.getBytes(), val);
				} else {
					throw new RedisException("没有找到指定的类型");
				}
			} else {
				if(type == STRING_TYPE.STRING && value instanceof String[]){
					String[] val = (String[]) value;
					result = jedis.rpush(key, val);
				} else if(type == STRING_TYPE.STRING && value instanceof String){
					result = jedis.rpush(key, value.toString());
				} else if(type == STRING_TYPE.BINARY && value instanceof byte[][]){
					byte[][] val = (byte[][]) value;
					result = jedis.rpush(key.getBytes(), val);
				} else if(type == STRING_TYPE.BINARY && value instanceof byte[]){
					byte[] val = (byte[]) value;
					result = jedis.rpush(key.getBytes(), val);
				} else {
					throw new RedisException("没有找到指定的类型");
				}
			}
		}
		return result;
	}
	
	public Object pop(Jedis jedis,STRING_TYPE type,String key) throws RedisException{
		Object result = null;
		if(this.direction == 0){
			if(type == STRING_TYPE.STRING){
				result = jedis.lpop(key);
			} else if(type == STRING_TYPE.BINARY){
				result = jedis.lpop(key.getBytes());
			} else {
				throw new RedisException("没有找到指定的类型");
			}
		} else {
			if(type == STRING_TYPE.STRING){
				result = jedis.rpop(key);
			} else if(type == STRING_TYPE.BINARY){
				result = jedis.rpop(key.getBytes());
			} else {
				throw new RedisException("没有找到指定的类型");
			}
		}
		return result;
	}
}
