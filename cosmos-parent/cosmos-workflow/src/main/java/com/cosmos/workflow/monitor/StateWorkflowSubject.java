package com.cosmos.workflow.monitor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StateWorkflowSubject {
	
	public static StateWorkflowSubject getInstance(){
		return Instance.instance;
	}
	
	private static class Instance  
    {  
        private static StateWorkflowSubject instance = new StateWorkflowSubject();  
    }  
	
	private StateWorkflowSubject(){
		
	}
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private Set<IStateWorkflowObserver> obs = new HashSet<IStateWorkflowObserver>();
	
	public void attach(IStateWorkflowObserver ob){
		lock.writeLock().lock();
		obs.add(ob);
		lock.writeLock().unlock();
	}
	
	public void detach(IStateWorkflowObserver ob){
		lock.writeLock().lock();
		ob.clear();
		obs.remove(ob);
		lock.writeLock().unlock();
	}
	
	public void notify(String taskId,String workflowId,String currentStateId){
		lock.readLock().lock();
		Iterator<IStateWorkflowObserver> iterator = obs.iterator();
		while(iterator.hasNext()){
			IStateWorkflowObserver item = iterator.next();
			if(taskId.equals(item.getTaskId()) 
					&& workflowId.equals(item.getWorkflowId()) 
					&& currentStateId.equals(item.getCurrentStateId())){
				if(item.isOpen()){
					item.updata(currentStateId);
				} else {
					item.clear();
					iterator.remove();
				}
			} 
		}
		lock.readLock().unlock();
	}
}
