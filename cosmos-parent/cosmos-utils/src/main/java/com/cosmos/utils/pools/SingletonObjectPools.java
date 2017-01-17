package com.cosmos.utils.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class SingletonObjectPools {
	
	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private static Map<String, Object> singletonPool = new HashMap<String, Object>();
	
	public static void registerSingletonObject(Object instance){
		if(instance != null){
			String key = instance.getClass().getName();
			lock.readLock().lock();
			if(!singletonPool.containsKey(key)){
				lock.readLock().unlock();
				lock.writeLock().lock();
				if(!singletonPool.containsKey(key)){
					singletonPool.put(key, instance);
				}
				lock.readLock().lock();
				lock.writeLock().unlock();
			}
			lock.readLock().unlock();
		}
	}
	
	public static boolean exists(String classPath){
		boolean result = false;
		lock.readLock().lock();
		result = singletonPool.containsKey(classPath);
		lock.readLock().unlock();
		return result;
	}
	
	public static boolean exists(Class<?> clazz){
		return exists(clazz.getName());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getSingletonObject(Class<T> clazz){
		T result = null;
		Object instance = getSingletonObject(clazz.getName());
		if(instance!=null){
			result = (T) instance;
		}
		return result;
	}
	
	public static Object getSingletonObject(String classPath){
		lock.readLock().lock();
		Object result = singletonPool.get(classPath);
		lock.readLock().unlock();
		return result;
	}
}
