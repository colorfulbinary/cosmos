package com.cosmos.workflow.activities.sequence.action.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class RedisSetHashDataActivity extends ActionActivity{
	
	public class FieldValue{
		
		private FieldValue(String fieldName,String value,NX_TYPE nxxx,STRING_TYPE type){
			this.fieldName = fieldName;
			this.value = value;
			this.nxxx = nxxx;
			this.type = type;
		}
		
		private String fieldName;
		
		private String value;
		
		private NX_TYPE nxxx;
		
		private STRING_TYPE type;
		
		public STRING_TYPE getType() {
			return type;
		}

		public void setType(STRING_TYPE type) {
			this.type = type;
		}
		
		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
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
		
	}
	
	public FieldValue creatFieldValue(String fieldName,String value,NX_TYPE nxxx,STRING_TYPE type){
		return new FieldValue(fieldName,value,nxxx,type);
	}
	
	private static final long serialVersionUID = 5066828791779543198L;
	
	private String client;
	
	private String key;
	
	private FieldValue[] value;
	
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
	
	public FieldValue[] getValue() {
		return value;
	}

	public void setValue(FieldValue[] value) {
		this.value = value;
	}

	@Override
	public void release() {
		this.client = null;
		this.key = null;
		this.value = null;
	}
	
	
	private Object exec(Jedis jedis,String key,ISequenceLogicData payload) throws WorkflowException{
		Object result = null;
		try {
			for (FieldValue item : this.value) {
				String fname = payload.getBoundValue(item.getFieldName());
				Object val = payload.get(item.getValue());
				if(StringUtils.isEmptyOrNull(fname) || val == null){
					throw new WorkflowException(this,"field与value不能为null");
				}
				NX_TYPE nxxx = item.getNxxx();
				STRING_TYPE type = item.getType();
				if(type == STRING_TYPE.STRING && val instanceof String){
					if(nxxx == NX_TYPE.NX){
						result = jedis.hsetnx(key, fname, val.toString());
					} else {
						result = jedis.hset(key, fname, val.toString());
					}
				} else if(type == STRING_TYPE.BINARY && val instanceof byte[]){
					if(nxxx == NX_TYPE.NX){
						result = jedis.hsetnx(key.getBytes(), fname.getBytes(), (byte[])val);
					} else {
						result = jedis.hset(key.getBytes(), fname.getBytes(), (byte[])val);
					}
				} else {
					throw new WorkflowException(this,"无法解析输入值，必须是一个String或byte[]");
				}
			}
		} catch (Exception e) {
			throw new WorkflowException(this,"设置Redis错误",e);
		}
		return result;
	}
	
	private Response<Long> exec(Pipeline pipe,String key,ISequenceLogicData payload) throws WorkflowException{
		Response<Long> result = null;
		try {
			for (FieldValue item : this.value) {
				String fname = payload.getBoundValue(item.getFieldName());
				Object val = payload.get(item.getValue());
				if(StringUtils.isEmptyOrNull(fname) || val == null){
					throw new WorkflowException(this,"field与value不能为null");
				}
				NX_TYPE nxxx = item.getNxxx();
				STRING_TYPE type = item.getType();
				if(type == STRING_TYPE.STRING && val instanceof String){
					if(nxxx == NX_TYPE.NX){
						result = pipe.hsetnx(key, fname, val.toString());
					} else {
						result = pipe.hset(key, fname, val.toString());
					}
				} else if(type == STRING_TYPE.BINARY && val instanceof byte[]){
					if(nxxx == NX_TYPE.NX){
						result = pipe.hsetnx(key.getBytes(), fname.getBytes(), (byte[])val);
					} else {
						result = pipe.hset(key.getBytes(), fname.getBytes(), (byte[])val);
					}
				} else {
					throw new WorkflowException(this,"无法解析输入值，必须是一个String或byte[]");
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
			if(StringUtils.isEmptyOrNull(keyVal)){
				throw new WorkflowException(this,"KEY与不能为null");
			}
			if(obj instanceof Jedis){
				Jedis jedis = (Jedis) obj;
				result = exec(jedis,keyVal,data);
				if(this.time > 0){
					jedis.pexpire(keyVal, this.time);
				}
			} else if(obj instanceof Pipeline){
				Pipeline pipe = (Pipeline) obj;
				result = exec(pipe,keyVal,data);
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
