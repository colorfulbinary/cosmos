package com.cosmos.workflow.runtime.factory.sequence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowFactory;
import com.cosmos.workflow.runtime.factory.WorkflowInformation;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;
import com.cosmos.workflow.runtime.factory.WorkflowItem;

public class SequenceWorkflowFactory extends WorkflowFactory<SequenceActivity> {
		
	private Map<String, WorkflowItem<SequenceActivity>> mapping = new HashMap<String, WorkflowItem<SequenceActivity>>();
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	
	@Override
	public void register(WorkflowInformation<SequenceActivity>...wis)
			throws WorkflowRuntimeException {
		Map<String, WorkflowItem<SequenceActivity>> temp = new HashMap<String, WorkflowItem<SequenceActivity>>();
		for (WorkflowInformation<SequenceActivity> wi : wis) {
			WorkflowInstance<SequenceActivity>[] items = wi.getItems(false);
			for (WorkflowInstance<SequenceActivity> instance : items) {
				WorkflowItem<SequenceActivity> item = new WorkflowItem<SequenceActivity>();
				item.setInformation(wi);
				String activityId = instance.getKey();
				item.setKey(activityId);
				temp.put(activityId, item);
			}
		}
		lock.writeLock().lock();
		mapping.putAll(temp);
		lock.writeLock().unlock();
		temp.clear();
	}
	
	@Override
	public WorkflowInstance<SequenceActivity> getWorkflowInstance(String key) throws WorkflowRuntimeException {
		WorkflowInstance<SequenceActivity> result = null;
		lock.readLock().lock();
		try {
			if(this.mapping.containsKey(key)){
				WorkflowItem<SequenceActivity> item = this.mapping.get(key);
				result = item.getInstance();
			}
		} finally {
			lock.readLock().unlock();
		}
		return result;
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
		Iterator<Entry<String, WorkflowItem<SequenceActivity>>> iterator = mapping.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, WorkflowItem<SequenceActivity>> item = iterator.next();
			item.getValue().clear();
		}
		lock.writeLock().unlock();
		
	}
	
//	public static void main(String[] args) throws Exception {
//		InputStream c1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("default-workflow-activity-config.xml");
//		XmlFileActivityConfig.getInstance(c1);
//		InputStream c2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("demo.xml");
//		SequenceXmlFileWorkflowInformation info = SequenceXmlFileWorkflowInformation.getInstance("test", c2);
//		SequenceWorkflowFactory f = WorkflowFactory.getWorkflowFactory(SequenceWorkflowFactory.class);
//		f.register(info);
//		SequenceActivity root = f.getWorkflowInstance("sequence1").getExecutableActivity(null);
//		root.execute(new SequenceDataSet(new HashMap<Object, Object>()));
//	}

}
