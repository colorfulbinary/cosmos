package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisSetDataActivity extends ActionActivity{

	private static final long serialVersionUID = -748665929354480871L;
	
	private static final String PX = "PX";
	
	private static final byte[] PX_BYTE = "PX".getBytes();

	private String client;
	
	private String key;
	
	private String value;
	
	private NX_TYPE nxxx;
	
	private STRING_TYPE type;
	
	public STRING_TYPE getType() {
		return type;
	}

	public void setType(STRING_TYPE type) {
		this.type = type;
	}

	private long time;
		
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

	public NX_TYPE getNxxx() {
		return nxxx;
	}

	public void setNxxx(NX_TYPE nxxx) {
		this.nxxx = nxxx;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public void release() {
		this.client =  null;
		this.key = null;
		this.value = null;
		this.nxxx = null;
		
	}
	
	private Object exec(Jedis jedis,String keyVal,Object val) throws WorkflowException{
		Object result = null;
		try {
			if(time > 0){
				if(this.type == STRING_TYPE.STRING && val instanceof String){
					if(this.nxxx == NX_TYPE.NONE){
						result = jedis.set(keyVal, val.toString());
						jedis.pexpire(keyVal, this.time);
					} else {
						result = jedis.set(keyVal, val.toString(), this.nxxx.toString(),PX,this.time);
					}
				} else if(this.type == STRING_TYPE.BINARY && val instanceof byte[]){
					if(this.nxxx == NX_TYPE.NONE){
						result = jedis.set(keyVal.getBytes(), (byte[])val);
						jedis.pexpire(keyVal, this.time);
					} else {
						result = jedis.set(keyVal.getBytes(), (byte[])val, this.nxxx.toByteArray(),PX_BYTE,this.time);
					}
				} else {
					throw new WorkflowException(this,"类型不匹配");
				}
			} else {
				if(this.type == STRING_TYPE.STRING && val instanceof String){
					if(this.nxxx == NX_TYPE.NONE){
						result = jedis.set(keyVal, val.toString());
					} else {
						result = jedis.set(keyVal, val.toString(), this.nxxx.toString());
					}
				} else if(this.type == STRING_TYPE.BINARY && val instanceof byte[]){
					if(this.nxxx == NX_TYPE.NONE){
						result = jedis.set(keyVal.getBytes(), (byte[])val);
					} else {
						result = jedis.set(keyVal.getBytes(), (byte[])val, this.nxxx.toByteArray());
					}
				} else {
					throw new WorkflowException(this,"类型不匹配");
				}
			}
		} catch (Exception e) {
			throw new WorkflowException(this,"设置Redis错误",e);
		}
		return result;
	}
	
	private Response<String> exec(Pipeline pipe,String keyVal,Object val) throws WorkflowException{
		Response<String> result = null;
		try {
			if(time > 0){
				if(this.type == STRING_TYPE.STRING && val instanceof String){
					if(this.nxxx == NX_TYPE.NONE){
						result = pipe.set(keyVal, val.toString());
					} else {
						result = pipe.set(keyVal, val.toString(), this.nxxx.toString());
					}
					pipe.pexpire(keyVal, this.time);
				} else if(this.type == STRING_TYPE.BINARY && val instanceof byte[]){
					if(this.nxxx == NX_TYPE.NONE){
						result = pipe.set(keyVal.getBytes(), (byte[])val);
					} else {
						result = pipe.set(keyVal.getBytes(), (byte[])val, this.nxxx.toByteArray());
					}
					pipe.pexpire(keyVal, this.time);
				} else {
					throw new WorkflowException(this,"类型不匹配");
				}
			} else {
				if(this.type == STRING_TYPE.STRING && val instanceof String){
					if(this.nxxx == NX_TYPE.NONE){
						result = pipe.set(keyVal, val.toString());
					} else {
						result = pipe.set(keyVal, val.toString(), this.nxxx.toString());
					}
				} else if(this.type == STRING_TYPE.BINARY && val instanceof byte[]){
					if(this.nxxx == NX_TYPE.NONE){
						result = pipe.set(keyVal.getBytes(), (byte[])val);
					} else {
						result = pipe.set(keyVal.getBytes(), (byte[])val, this.nxxx.toByteArray());
					}
				} else {
					throw new WorkflowException(this,"类型不匹配");
				}
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
			if(!StringUtils.isEmptyOrNull(this.out)){
				data.put(this.out, result);
			}
		} else {
			throw new WorkflowException(this,"没有找到要操作的client");
		}
	}
}
