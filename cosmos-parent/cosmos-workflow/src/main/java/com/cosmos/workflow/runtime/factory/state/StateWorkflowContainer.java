package com.cosmos.workflow.runtime.factory.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cosmos.workflow.activities.state.StateActivity;
import com.cosmos.workflow.runtime.factory.WorkflowContainer;
import com.cosmos.workflow.runtime.factory.WorkflowItem;

public class StateWorkflowContainer implements WorkflowContainer<StateActivity>{

	private static StateWorkflowContainer instance = new StateWorkflowContainer();
	
	private Map<String, WorkflowItem<StateActivity>> container = new HashMap<String, WorkflowItem<StateActivity>>();
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public static StateWorkflowContainer getInstance(){
		return StateWorkflowContainer.instance;
	}
	
	private StateWorkflowContainer(){
		
	}

	@Override
	public void put(Map<String, WorkflowItem<StateActivity>> items) {
		this.lock.writeLock().lock();
		Set<Entry<String,WorkflowItem<StateActivity>>> entrySet = items.entrySet();
		for (Entry<String, WorkflowItem<StateActivity>> entry : entrySet) {
			this.container.put(entry.getKey(),entry.getValue());
		}
		this.lock.writeLock().unlock();
	}

	@Override
	public void put(String key, WorkflowItem<StateActivity> item) {
		this.lock.writeLock().lock();
		this.container.put(key,item);
		this.lock.writeLock().unlock();
	}

	@Override
	public WorkflowItem<StateActivity> get(String key) {
		this.lock.readLock().lock();
		WorkflowItem<StateActivity> result = this.container.get(key);
		this.lock.readLock().unlock();
		return result;
	}

	@Override
	public void remove(String key) {
		this.lock.writeLock().lock();
		this.container.remove(key);
		this.lock.writeLock().unlock();
	}

	@Override
	public boolean clear() {
		boolean result = true;
		this.lock.writeLock().lock();
		Iterator<Entry<String, WorkflowItem<StateActivity>>> iterator = this.container.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, WorkflowItem<StateActivity>> item = iterator.next();
			WorkflowItem<StateActivity> wi = item.getValue();
			wi.clear();
		}
		this.lock.writeLock().unlock();
		return result;
	}

}
