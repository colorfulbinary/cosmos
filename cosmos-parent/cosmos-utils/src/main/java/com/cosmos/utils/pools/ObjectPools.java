package com.cosmos.utils.pools;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ObjectPools<T> {

	private Semaphore semaphore = null;

	private Map<T, Long> in = new ConcurrentHashMap<T, Long>();

	private Set<T> out = new HashSet<T>();

	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

	protected abstract T createObject() throws ObjectPoolsException;

	protected abstract int getMaxSize();

	protected abstract int getInitSize();

	protected abstract long getTimeout();

	public void clear(){
		Iterator<Entry<T, Long>> iterator = in.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<T, Long> item = iterator.next();
			if(item.getValue() + this.getTimeout() > System.currentTimeMillis()){
				iterator.remove();
			}
		}
	}
	
	public ObjectPools() {
		int initSize = this.getInitSize();
		int maxSize = getMaxSize();
		if (initSize > maxSize) {
			initSize = maxSize;
		}
		for (int i = 0; i < initSize; i++) {
			try {
				T obj = this.createObject();
				in.put(obj, System.currentTimeMillis());
			} catch (ObjectPoolsException e) {
				e.printStackTrace();
			}
		}
		semaphore = new Semaphore(maxSize);
	}
	
	private T getObject() throws ObjectPoolsException{
		T result = null;
		try {
			rwLock.readLock().lock();
			if (in.size() == 0) {
				rwLock.readLock().unlock();
				rwLock.writeLock().lock();
				try{
					if(in.size() == 0){
						result = this.createObject();
						out.add(result);
					}
				} finally {
					rwLock.readLock().lock();
					rwLock.writeLock().unlock();
				}
			} else {
				Iterator<Entry<T, Long>> i = in.entrySet().iterator();
				result = i.next().getKey();
				i.remove();
				out.add(result);
			}
		} catch (Exception e) {
			throw new ObjectPoolsException(e.getMessage(),e);
		} finally {
			rwLock.readLock().unlock();
		}
		return result;
	}

	public T checkOut() throws ObjectPoolsException {
		try {
			semaphore.acquire(1);
			System.out.println("解除阻塞");
		} catch (InterruptedException e) {
			throw new ObjectPoolsException(e.getMessage(),e);
		}
		return this.getObject();
	}

	public T checkOut(long timeout) throws ObjectPoolsException {
		T result = null;
		try {
			if(semaphore.tryAcquire(1, this.getTimeout(), TimeUnit.MILLISECONDS)){
				result = this.getObject();
			}
		} catch (Exception e) {
			throw new ObjectPoolsException(e.getMessage(),e);
		}
		return result;

	}

	public void checkIn(T obj) {
		rwLock.readLock().lock();
		if (out.contains(obj)) {
			rwLock.readLock().unlock();
			rwLock.writeLock().lock();
			if(out.contains(obj)){
				out.remove(obj);
				in.put(obj, System.currentTimeMillis());
				semaphore.release(1);
			}
			rwLock.readLock().lock();
			rwLock.writeLock().unlock();
		}
		rwLock.readLock().unlock();

	}
}
