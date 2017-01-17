package com.cosmos.workflow.activities.sequence.action.convertor;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class StringToInputStreamActivity extends ActionActivity{
	
	private static final long serialVersionUID = 8213378604039519483L;
	
	private String charset;
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}


	@Override
	public void release() {
		charset = null;
		
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object value = data.get(this.in);
		if (value == null || !(value instanceof String)) {
			WorkflowException e = new WorkflowException(this,"要转换的字符串为空或不是一个字符串");
			boolean isCompensated = this.compensate(data, e);
			throw new WorkflowException(this,e.getMessage() + "--->" + (isCompensated ? "已经执行补偿":"未执行补偿"));
		}
		byte[] bytes = null;
		try {
			bytes = value.toString().getBytes(this.charset);
		} catch (UnsupportedEncodingException e) {
			boolean isCompensated = this.compensate(data, e);
			throw new WorkflowException(this,"将字符串转换为字节时发生错误:" + e.getMessage() + "--->" + (isCompensated ? "已经执行补偿":"未执行补偿"));
		}
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		data.put(this.out, bin);
	}

}
