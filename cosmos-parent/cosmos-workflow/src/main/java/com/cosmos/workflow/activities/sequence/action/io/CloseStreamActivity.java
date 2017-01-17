package com.cosmos.workflow.activities.sequence.action.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class CloseStreamActivity extends ActionActivity{

	private static final long serialVersionUID = 2453373794490891421L;

	@Override
	public void release() {
		
	}
	
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object stream = data.get(this.in);
		if(stream == null){
			throw new WorkflowException(this,"没有找到要关闭的stream");
		} else {
			if(stream instanceof InputStream){
				try {
					((InputStream)stream).close();
				} catch (IOException e) {
					throw new WorkflowException(this,"关闭输入流失败:" + e.getMessage(),e);
				}
			} else if(stream instanceof OutputStream){
				try {
					((OutputStream)stream).close();
				} catch (IOException e) {
					throw new WorkflowException(this,"关闭输出流失败:" + e.getMessage(),e);
				}
			} else {
				throw new WorkflowException(this,"高关闭的不是一个流对象");
			}
		}
	}

}
