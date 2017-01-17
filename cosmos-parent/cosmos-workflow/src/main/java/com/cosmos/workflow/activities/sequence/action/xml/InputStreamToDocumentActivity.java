package com.cosmos.workflow.activities.sequence.action.xml;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class InputStreamToDocumentActivity extends ActionActivity{
	
	private static final long serialVersionUID = -6651244808540996723L;

	@Override
	public void release() {
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
			SAXReader reader = new SAXReader();
			Document readValue = reader.read((InputStream)inputStream);
			((InputStream)inputStream).close();
			data.put(this.out, readValue);
		} catch (Exception e) {
			throw new WorkflowException(this,"无法转换输入流中的XML数据:" + e.getMessage(),e);
		}
	}
	
}
