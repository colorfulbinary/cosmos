package com.cosmos.workflow.activities.sequence.action.convertor;

import java.io.IOException;
import java.io.InputStream;

import com.cosmos.utils.io.StreamProcessor;
import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class InputStreamToStringActivity extends ActionActivity{
	
	private static final long serialVersionUID = 6216366310114912716L;

	private String charset;
	
	private int length;
	
	@Override
	public void release() {
		this.charset = null;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object inputStream = null;
		if(!StringUtils.isEmptyOrNull(this.in)){
			inputStream = data.get(this.in);
		}
		if (inputStream == null) {
			throw new WorkflowException(this, "没有找到要转换的输入流");
		} 
		if(!(inputStream instanceof InputStream)){
			throw new WorkflowException(this, "要转换的不是一个输入刘");
		}
		try {
			byte[] readByte = null;
			if(this.length > 0){
				readByte = StreamProcessor.readByte((InputStream)inputStream, this.length);
			} else {
				readByte = StreamProcessor.readByte((InputStream)inputStream);
			}
			String result = new String(readByte,this.charset);
			data.put(this.out, result);
		} catch (IOException e) {
			throw new WorkflowException(this,"从输入流读取数据失败:" + e.getMessage(),e);
		}
	}

}
