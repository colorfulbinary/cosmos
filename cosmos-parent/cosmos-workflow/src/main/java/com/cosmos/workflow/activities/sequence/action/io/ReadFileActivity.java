package com.cosmos.workflow.activities.sequence.action.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class ReadFileActivity extends ActionActivity {
	
	private static final long serialVersionUID = -3713360764266794783L;
	
	private String filePath;

	@Override
	public void release() {
		this.filePath = null;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		String path = null;
		if(this.filePath.startsWith("{")){
			Object ph = data.get(this.filePath.substring(1,this.filePath.length() - 1));
			if(ph!=null && !ph.toString().trim().equals("")){
				path = ph.toString();
			} else {
				throw new WorkflowException(this,"找不到绑定文件路径");
			}
		} else {
			path = this.filePath;
		}
		try {
			FileInputStream in = new FileInputStream(path);
			data.put(this.out, in);
		} catch (FileNotFoundException e) {
			throw new WorkflowException(this,"没有找到要读取的文件:" + e.getMessage(),e);
		}
	}
}
