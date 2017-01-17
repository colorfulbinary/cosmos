package com.cosmos.workflow.activities.sequence.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.SQActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class ForeachActivity extends LogicActivity{

	private static final long serialVersionUID = 2080838714852125211L;

	private List<SQActivity> activities = new ArrayList<SQActivity>();
	
	private String in;
	
	private String out;

	public String getIn() {
		return in;
	}

	public void setIn(String in) {
		this.in = in;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	@Override
	public void release() {
		
	}

	@Override
	public void appendChild(SQActivity act) {
		activities.add(act);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(ISequenceLogicData data) throws WorkflowException {
		data.setBreak(false);
		data.setContinue(false);
		Object obj = data.get(this.in);
		Collection list = null;
		if(obj instanceof Collection<?>){
			list = (Collection)obj;
		} else if(obj instanceof Map){
			list = ((Map)obj).entrySet();
		} else if(obj instanceof Object[]){
			list = Arrays.asList((Object[])obj);
		} else {
			throw new WorkflowException(this,"无法解析迭代类型");
		}
		Iterator iterator = list.iterator();
		int index = 0;
		exit_break:while(iterator.hasNext()){
			Object item = iterator.next();
			data.put(this.out, item);
			data.setIndex(index++);
			for(int i=0,len = this.activities.size();i<len;i++){
				try {
					activities.get(i).execute(data);
				} catch (WorkflowException e) {
					if(!this.isExceptionOnContinue()){
						throw e;
					}
				}
				if(data.isBreak()){
					break exit_break;
				} else if(data.isContinue()){
					continue exit_break;
				}
			}
		}
	}	
}
