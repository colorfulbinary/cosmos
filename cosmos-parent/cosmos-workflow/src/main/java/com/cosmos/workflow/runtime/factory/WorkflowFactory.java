package com.cosmos.workflow.runtime.factory;

import com.cosmos.utils.pools.SingletonObjectPools;
import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;

public abstract class WorkflowFactory<T extends Activity> {
		
	public final static WorkflowFactory<?> getWorkflowFactory(String factoryPath) throws WorkflowRuntimeException{
		Object factory = SingletonObjectPools.getSingletonObject(factoryPath);
		if(factory == null || !(factory instanceof WorkflowFactory)){
			throw new WorkflowRuntimeException("没有找到对应的工作流工厂");
		}
		return (WorkflowFactory<?>)factory;
	}
	
	@SuppressWarnings("unchecked")
	public final static <T extends Activity> WorkflowFactory<T> getWorkflowFactory(String factoryPath,Class<T> clazz) throws WorkflowRuntimeException{
		Object factory = SingletonObjectPools.getSingletonObject(factoryPath);
		if(factory == null || !(factory instanceof WorkflowFactory)){
			throw new WorkflowRuntimeException("没有找到对应的工作流工厂");
		}
		WorkflowFactory<T> result = null;
		try{
			result = (WorkflowFactory<T>)factory;
		} catch(Exception e){
			throw new WorkflowRuntimeException("类型不匹配");
		}
		return result;
	}
		
	//清理流程
	public abstract void clear();
	
	//注册工作流
	public abstract void register(WorkflowInformation<T>...wi) throws WorkflowRuntimeException ;
	
	//获取指定工作流实例
	public abstract WorkflowInstance<T> getWorkflowInstance(String key) throws WorkflowRuntimeException ;
	
	
	
	
}
