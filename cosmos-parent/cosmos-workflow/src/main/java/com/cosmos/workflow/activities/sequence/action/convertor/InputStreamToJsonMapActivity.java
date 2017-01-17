package com.cosmos.workflow.activities.sequence.action.convertor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InputStreamToJsonMapActivity extends ActionActivity{

	private static final long serialVersionUID = -6384958355823574950L;
	
	private ObjectMapper maper = new ObjectMapper();
	
	@Override
	public void release() {
		this.maper = null;
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
			throw new WorkflowException(this,"要转换的不是一个输入流对象!");
		}
		try {
			Map<?,?> readValue = this.maper.readValue((InputStream)inputStream, Map.class);
			data.put(this.out, readValue);
		} catch (IOException e) {
			throw new WorkflowException(this,"无法转换输入流中的JSON数据:" + e.getMessage(),e);
		}

	}

}
