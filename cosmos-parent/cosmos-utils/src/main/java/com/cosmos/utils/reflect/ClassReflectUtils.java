package com.cosmos.utils.reflect;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class ClassReflectUtils {
	
	private static HashMap<String, Class<?>> baseType = new HashMap<String, Class<?>>();
	
	static {
		baseType.put("int", int.class);
		baseType.put("short", short.class);
		baseType.put("byte", byte.class);
		baseType.put("boolean", boolean.class);
		baseType.put("long", long.class);
		baseType.put("float", float.class);
		baseType.put("double", double.class);
		baseType.put("char", char.class);
	}
	
	public static Class<?> getClassFromString(String path) throws ClassNotFoundException{
		if(baseType.containsKey(path)){
			return baseType.get(path);
		} else {
			return Class.forName(path);
		}
	}

	
	public static <T> T getInstance(Class<T> clazz) throws ReflectException{
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReflectException(e.getMessage(),e);
		} 
	}
	
	public static Object getInstance(String classPath) throws ReflectException{
		try {
			Class<?> clazz = getClassFromString(classPath);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReflectException(e.getMessage(),e);
		} 
	}
	
	public static <T> T getInstance(Class<T> clazz,Class<?>[] types,Object[] args) throws ReflectException{
		try {
			Constructor<T> constructor = clazz.getConstructor(types);
			if(types == null){
				types = new Class[0];
			}
			if(args == null){
				args = new Object[0];
			}
			return constructor.newInstance(args);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReflectException(e.getMessage(),e);
		}
	}
	
	public static Object getInstance(String classPath,Class<?>[] types,Object[] args) throws ReflectException{
		try {
			Class<?> clazz = ClassReflectUtils.getClassFromString(classPath);
			if(types == null){
				types = new Class[0];
			}
			if(args == null){
				args = new Object[0];
			}
			return ClassReflectUtils.getInstance(clazz, types, args);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
	}
	
	public static <T> T getInstance(Class<T> clazz,String[] types,Object[] args) throws ReflectException{
		try {
			Class<?>[] argsType = null;
			if(types == null){
				argsType = new Class[0];
			} else {
				argsType = new Class[types.length];
			}
			if(args == null){
				args = new Object[0];
			}
			for (int i = 0,len= types.length; i < len; i++) {
				argsType[i] = ClassReflectUtils.getClassFromString(types[i]);
			}
			return ClassReflectUtils.getInstance(clazz, argsType, args);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
	}
	
	public static Object getInstance(String classPath,String[] types,Object[] args) throws ReflectException{
		try {
			Class<?> clazz = ClassReflectUtils.getClassFromString(classPath);
			return ClassReflectUtils.getInstance(clazz, types, args);
		} catch (ClassNotFoundException e) {
			throw new ReflectException(e.getMessage(),e);
		}
			
	}
}
