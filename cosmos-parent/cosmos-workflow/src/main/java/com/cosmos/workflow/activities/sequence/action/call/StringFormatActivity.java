package com.cosmos.workflow.activities.sequence.action.call;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class StringFormatActivity extends ActionActivity{
	
	private static final long serialVersionUID = 1445243794260884034L;

	private String source;
	
	private String[] args;
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public void release() {
		this.args = null;
		this.source = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		String src = null;
		if(this.source.startsWith("{")){
			Object ph = data.get(this.source.substring(1,this.source.length() - 1));
			if(ph!=null && !ph.toString().trim().equals("")){
				src = ph.toString();
			} else {
				throw new WorkflowException(this,"找不到绑定文件路径");
			}
		} else {
			src = this.source;
		}
		String result = this.args.length >0 ? String.format(src, data.getValueByNames(this.args)) :  src;
		data.put(this.out, result);
	}

}
