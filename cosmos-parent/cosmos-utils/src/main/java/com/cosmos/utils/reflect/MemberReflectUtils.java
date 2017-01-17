package com.cosmos.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberReflectUtils<T> {
	
	private T instance;
	
	private Class<T> clazz;
	
	private boolean isFieldBuffer;
	
	private boolean isMethodBuffer;
	
	private Map<String, Field> fieldBuffer;
	
	private Map<MethodInfo,Method> methodBuffer;
	
	private class MethodInfo{
				
		private Class<?>[] methodTypes;
		
		private String methodName;
		
		public Class<?>[] getMethodTypes() {
			return methodTypes;
		}

		public void setMethodTypes(Class<?>... methodTypes) {
			this.methodTypes = methodTypes;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
		
		@Override
		public boolean equals(Object obj) {
			boolean result = false;
			if(obj instanceof MemberReflectUtils.MethodInfo){
				@SuppressWarnings({ "unchecked", "rawtypes" })
				MethodInfo info = (MemberReflectUtils.MethodInfo) obj;
				if(this.methodName.equals(info.getMethodName())){
					Class<?>[] mts = info.getMethodTypes();
					if(mts == null){
						mts = new Class<?>[0];
					}
					if(mts.length == this.methodTypes.length){
						boolean isSuccess = true;
						for(int i=0,len = mts.length;i < len;i++){
							if(mts[i] != this.methodTypes[i]){
								isSuccess = false;
								break;
							}
						}
						result = isSuccess;
					}
				} 
			}
			return result;
		}
	}
	
	@SuppressWarnings("unchecked")
	public MemberReflectUtils(T instance){
		
		this.clazz = (Class<T>) instance.getClass();
		this.instance = instance;
		
	}
	
	public MemberReflectUtils(T instance,boolean useFieldBuffer,boolean useMethodBuffer){
		this(instance);
		this.isFieldBuffer = useFieldBuffer;
		this.isMethodBuffer = useMethodBuffer;
		if(isFieldBuffer){
			this.fieldBuffer = new ConcurrentHashMap<String, Field>(16);
		}
		
		if(isMethodBuffer){
			this.methodBuffer = new ConcurrentHashMap<MethodInfo,Method>(16);
		}
	}
	
	private Field getFieldInstance(String fieldName) throws NoSuchFieldException, SecurityException {
		Field field = null;
		if(this.isFieldBuffer && this.fieldBuffer.containsKey(fieldName)){
			field = this.fieldBuffer.get(fieldName);
		} else {
			field = this.clazz.getDeclaredField(fieldName);
			if(this.isFieldBuffer){
				this.fieldBuffer.put(fieldName, field);
			}
		}
		return field;
	}
	
	private Method getMethodInstance(String methodName,Class<?>...types) throws NoSuchMethodException, SecurityException {
		Method method = null;
		MethodInfo info = new MethodInfo();
		info.setMethodName(methodName);
		info.setMethodTypes(types);
		if(this.isMethodBuffer && this.methodBuffer.containsKey(info)){
			method = this.methodBuffer.get(info);
		} else {
			method = this.clazz.getMethod(methodName, types);
			if(this.isMethodBuffer){
				this.methodBuffer.put(info, method);
			}
		}
		return method;
	}
	
	@SuppressWarnings("unchecked")
	public <V> V getFieldValue(String fieldName,Class<V> clazz) throws ReflectException{
		try {
			
			return (V) getFieldValue(fieldName);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
	}
	
	public Object getFieldValue(String fieldName) throws ReflectException{
		try {
			Field field = this.getFieldInstance(fieldName);
			field.setAccessible(true);
			Object val = field.get(this.instance);
			return val;
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
	}
	
	public void setFieldValue(String fieldName,Object value) throws ReflectException{
		try {
			Field field = this.getFieldInstance(fieldName);
			field.setAccessible(true);
			field.set(this.instance, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ReflectException(e.getMessage(),e);
		} 
	}
	
	public <A extends Annotation> A getFiledAnnotation(String fieldName,Class<A> annotation) throws ReflectException{
		try {
			Field field = this.getFieldInstance(fieldName);
			field.setAccessible(true);
			A result = field.getAnnotation(annotation);
			return result;
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
	}
	
	public Annotation[] getFiledAnnotations(String fieldName) throws ReflectException{
		try {
			Field field = this.getFieldInstance(fieldName);
			field.setAccessible(true);
			Annotation[] result = field.getAnnotations();
			return result;
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
	}
	
	public String[] getAllFieldNames(){
		Field[] declaredFields = this.clazz.getDeclaredFields();
		String[] names = new String[declaredFields.length];
		for (int i=0,len =declaredFields.length;i<len;i++) {
			names[i] = declaredFields[i].getName();
			if(this.isFieldBuffer && !this.fieldBuffer.containsKey(names[i])){
				this.fieldBuffer.put(names[i], declaredFields[i]);
			}
		}
		return names;
	}
	
	public Object invoke(String methodName,Class<?>[] types,Object[] args) throws ReflectException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method method;
		try {
			method = this.getMethodInstance(methodName, types);
			
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return method.invoke(this.instance, args);
	}
	
	public Object invoke(String methodName,String[] types,Object[] args) throws ReflectException{
		Class<?>[] mtypes = null;
		Object result;
		try {
			if(types == null){
				types = new String[0];
			}
			mtypes = new Class<?>[types.length];
			for(int i=0,len = mtypes.length;i < len ;i++){
				mtypes[i] = ClassReflectUtils.getClassFromString(types[i]);
			}
			result = this.invoke(methodName, mtypes, args);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return result;
	}	
	
	
	public static Object invoke(Class<?> clazz,String methodName,Class<?>[] types,Object[] args) throws ReflectException{
		Method method;
		try {
			method = clazz.getMethod(methodName, types);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
		Object result;
		try {
			result = method.invoke(null, args);
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		} 
		return result;
	}
	
	public static Object invoke(Class<?> clazz,String methodName,String[] types,Object[] args) throws ReflectException{
		Class<?>[] mtypes = null;
		try {
			if(types == null){
				types = new String[0];
			}
			mtypes = new Class<?>[types.length];
			for(int i=0,len = mtypes.length;i < len ;i++){
				mtypes[i] = ClassReflectUtils.getClassFromString(types[i]);
			}
		} catch (Exception e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return invoke(clazz,methodName, mtypes, args);
	}
	
	public static Object invoke(String classPath,String methodName,Class<?>[] types,Object[] args) throws ReflectException{
		Class<?> clazz;
		try {
			clazz = ClassReflectUtils.getClassFromString(classPath);
		} catch (ClassNotFoundException e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return invoke(clazz,methodName,types,args);
	}
	
	public static Object invoke(String classPath,String methodName,String[] types,Object[] args) throws ReflectException{
		Class<?> clazz;
		try {
			clazz = ClassReflectUtils.getClassFromString(classPath);
		} catch (ClassNotFoundException e) {
			throw new ReflectException(e.getMessage(),e);
		}
		return invoke(clazz,methodName,types,args);
	}
}
