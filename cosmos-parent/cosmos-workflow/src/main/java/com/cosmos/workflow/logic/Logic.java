package com.cosmos.workflow.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;


public abstract class Logic {
	
	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private static Map<String, Class<?>> logicClasses = new HashMap<String, Class<?>>();
	
	public abstract ISequenceLogicData execute(ISequenceLogicData payload) throws Exception;
	
	public static String getImportString(String pkgs){
		StringBuilder importStr = new StringBuilder("");
		if(pkgs!=null && !"".equals(pkgs.trim())){
			String[] split = pkgs.split(",");
			for (String pkg : split) {
				if(!"".equals(pkg.trim())){
					importStr.append("import ").append(pkg).append(";");
				}
			}
		}
		return importStr.toString();
	}
	
	public static Logic getLogic(String className,String sourceCode,String importCode ,String logicString) throws CompileException{
		Logic result = null;
		String code = String.format(sourceCode,Logic.getImportString(importCode),className,logicString);
		//System.out.println(code);
		Object obj = JavaCompileEngine.getInstance().getObject(className,code, null);
		if(obj instanceof Logic){
			result = (Logic) obj;
		}
		return result;
	}
	
	public static Logic getLogic(String className,String sourceCode,String importCode ,String logicString,String tagId) throws CompileException{
		Logic result = null;
		if(tagId == null || "".equals(tagId.trim())){
			result =  Logic.getLogic(className, sourceCode, importCode, logicString);
		} else {
			lock.readLock().lock();
			Object obj = null;
			if(!logicClasses.containsKey(tagId)){
				lock.readLock().unlock();
				lock.writeLock().lock();
				if(!logicClasses.containsKey(tagId)){
					String code = String.format(sourceCode,Logic.getImportString(importCode),className,logicString);
					try {
						obj = JavaCompileEngine.getInstance().getObject(className,code, null);
						logicClasses.put(tagId, obj.getClass()); 
						lock.readLock().lock();
					} catch (CompileException e) {
						throw e;
					} finally{
						lock.writeLock().unlock();
					}
				}
			} else {
				Class<?> clazz = logicClasses.get(tagId);
				try {
					obj = clazz.newInstance();
				} catch (Exception e) {
					lock.readLock().unlock();
					throw new CompileException(null,"编译成功，获取实例时发生异常:" + e.getMessage());
				}
			}
			if(obj instanceof Logic){
				result = (Logic) obj;
			}
			lock.readLock().unlock();
		}
		return result;
	}	
}
