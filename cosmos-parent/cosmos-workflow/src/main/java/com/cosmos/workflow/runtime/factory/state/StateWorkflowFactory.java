package com.cosmos.workflow.runtime.factory.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cosmos.workflow.activities.state.StateActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowFactory;
import com.cosmos.workflow.runtime.factory.WorkflowInformation;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;
import com.cosmos.workflow.runtime.factory.WorkflowItem;

public class StateWorkflowFactory extends WorkflowFactory<StateActivity>{
		
	private Map<String, WorkflowItem<StateActivity>> mapping = new HashMap<String, WorkflowItem<StateActivity>>();
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		
	@Override
	public void clear() {
		lock.writeLock().lock();
		Iterator<Entry<String, WorkflowItem<StateActivity>>> iterator = mapping.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, WorkflowItem<StateActivity>> item = iterator.next();
			item.getValue().clear();
		}
		lock.writeLock().unlock();
	}

	@Override
	public void register(WorkflowInformation<StateActivity>...wis)
			throws WorkflowRuntimeException {
		Map<String, WorkflowItem<StateActivity>> temp = new HashMap<String, WorkflowItem<StateActivity>>();
		for (WorkflowInformation<StateActivity> wi : wis) {
			WorkflowItem<StateActivity> result = new WorkflowItem<StateActivity>();
			WorkflowInstance<StateActivity> item = wi.getItemById(null);
			if(item == null){
				throw new WorkflowRuntimeException("没有找到流程起始活动");
			}
			result.setInformation(wi);
			result.setInstance(item);
			result.setKey(wi.getWorkflowId());
			temp.put(wi.getWorkflowId(), result);
		}
		lock.writeLock().lock();
		this.mapping.putAll(temp);
		lock.writeLock().unlock();
		temp.clear();
	}

	@Override
	public WorkflowInstance<StateActivity> getWorkflowInstance(String key)
			throws WorkflowRuntimeException {
		WorkflowInstance<StateActivity> result = null;
		lock.readLock().lock();
		try {
			if(this.mapping.containsKey(key)){
				WorkflowItem<StateActivity> item = this.mapping.get(key);
				result = item.getInstance();
			}
		} finally {
			lock.readLock().unlock();
		}
		return result;
	}
}
