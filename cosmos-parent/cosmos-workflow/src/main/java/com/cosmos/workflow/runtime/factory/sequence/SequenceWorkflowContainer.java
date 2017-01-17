package com.cosmos.workflow.runtime.factory.sequence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.runtime.factory.WorkflowContainer;
import com.cosmos.workflow.runtime.factory.WorkflowItem;


public class SequenceWorkflowContainer implements WorkflowContainer<SequenceActivity>{

	private static SequenceWorkflowContainer instance = new SequenceWorkflowContainer();
	
	private Map<String, WorkflowItem<SequenceActivity>> container = new HashMap<String, WorkflowItem<SequenceActivity>>();
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public static SequenceWorkflowContainer getInstance(){
		return SequenceWorkflowContainer.instance;
	}
	
	private SequenceWorkflowContainer(){
		
	}

	@Override
	public void put(Map<String, WorkflowItem<SequenceActivity>> items) {
		this.lock.writeLock().lock();
		Set<Entry<String,WorkflowItem<SequenceActivity>>> entrySet = items.entrySet();
		for (Entry<String, WorkflowItem<SequenceActivity>> entry : entrySet) {
			this.container.put(entry.getKey(),entry.getValue());
		}
		this.lock.writeLock().unlock();
	}

	@Override
	public void put(String key, WorkflowItem<SequenceActivity> item) {
		this.lock.writeLock().lock();
		this.container.put(key,item);
		this.lock.writeLock().unlock();
	}

	@Override
	public WorkflowItem<SequenceActivity> get(String key) {
		this.lock.readLock().lock();
		WorkflowItem<SequenceActivity> result = this.container.get(key);
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
		Iterator<Entry<String, WorkflowItem<SequenceActivity>>> iterator = this.container.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, WorkflowItem<SequenceActivity>> item = iterator.next();
			WorkflowItem<SequenceActivity> wi = item.getValue();
			wi.clear();
		}
		this.lock.writeLock().unlock();
		return result;
	}
}
