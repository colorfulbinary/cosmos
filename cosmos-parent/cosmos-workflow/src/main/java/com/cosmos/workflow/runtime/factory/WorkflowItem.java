package com.cosmos.workflow.runtime.factory;

import java.lang.ref.SoftReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;

public class WorkflowItem<T extends Activity>{
	
	private String key;
	
	private WorkflowInformation<T> information;
	
	private SoftReference<WorkflowInstance<T>> instance;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public WorkflowInformation<T> getInformation() {
		return information;
	}

	public void setInformation(WorkflowInformation<T> information) {
		this.information = information;
	}
	
	public void clear(){
		lock.writeLock().lock();
		WorkflowInstance<T> result = null;
		if(this.instance != null && (result = this.instance.get()) != null){
			long timeout = result.getTimeout();
			if(timeout > 0 && (System.currentTimeMillis() - result.getTime()) > timeout){
				this.instance = null;
				//System.out.println("清理:" + timeout);
			}
		}
		lock.writeLock().unlock();
	}

	public WorkflowInstance<T> getInstance() throws WorkflowRuntimeException {
		WorkflowInstance<T> result =  null;
		lock.readLock().lock();
		if(this.instance == null || (result = this.instance.get()) == null){
			lock.readLock().unlock();
			lock.writeLock().lock();
			try {
				if(this.instance == null ||(result = this.instance.get()) == null){
					result = this.information.getItemById(this.key);
					if(result != null){
						this.instance = new SoftReference<WorkflowInstance<T>>(result);
					} else {
						throw new WorkflowRuntimeException("没有找到指定ID的实例");
					}
				}
				lock.readLock().lock();
			} finally {
				lock.writeLock().unlock();
			}
		}
		if(result != null){
			result.setTime(System.currentTimeMillis());
		}
		lock.readLock().unlock();
		return result;
	}

	public void setInstance(WorkflowInstance<T> instance) {
		lock.writeLock().lock();
		if(instance == null){
			this.instance = null;
		} else {
			this.instance = new SoftReference<WorkflowInstance<T>>(instance);
		}
		lock.writeLock().unlock();
	}
}
