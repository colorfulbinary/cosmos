package com.cosmos.utils.ognl;

import java.util.Map;

import com.cosmos.utils.reflect.MemberReflectUtils;
import com.cosmos.utils.reflect.ReflectException;

public class OgnlContext {
	
	public static Object execute(Object instance,String ognlString,Object...args) throws OgnlException{
		Object result = null;
		if(ognlString == null || ognlString.trim().equals("")){
			throw new OgnlException("缺失表达式");
		}
		String[] ognlList = ognlString.split("->");
		result = OgnlContext.executeOgnl(instance, ognlList, 0, args, 0);
		return result;
	}
	
	private static Object executeOgnl(Object instance,String[] ognl,int index,Object[] args,int argsIndex)throws OgnlException{
		Object result = null;
		String ognlString = ognl[index];
		if(ognlString == null || ognlString.trim().equals("")){
			throw new OgnlException("缺失表达式");
		}
		if(instance == null){
			throw new OgnlException("缺失执行实例");
		}
		
		if(ognlString.startsWith("@")){
			//实例方法
			MemberReflectUtils<Object> obj = new MemberReflectUtils<Object>(instance);
			String methodName = ognlString.substring(1,ognlString.indexOf('('));
			String argsString = ognlString.substring(ognlString.indexOf('(') + 1,ognlString.indexOf(')'));
			String[] types;
			if(argsString.trim().equals("")){
				types = new String[0];
			} else {
				types = argsString.split(",");
			}
			Object[] argsObj = new Object[types.length];
			for (int i = 0,len = types.length; i < len; i++) {
				argsObj[i] = args[argsIndex++];
			}
			try {
				result = obj.invoke(methodName, types, args);
			} catch (Exception e) {
				throw new OgnlException("实例方法" + methodName + "执行错误:" + e.getLocalizedMessage(),e);
			} 
		} else if(ognlString.startsWith("#")){
			//Map
			if(instance instanceof Map<?, ?>){
				Map<?, ?> mp = (Map<?, ?>) instance;
				result = mp.get(ognlString.substring(1));
			} else {
				throw new OgnlException("无法将对象解析为Map");
			}
		} else {
			//属性
			MemberReflectUtils<Object> obj = new MemberReflectUtils<Object>(instance);
			try {
				result = obj.getFieldValue(ognlString, Object.class);
			} catch (ReflectException e) {
				throw new OgnlException("无法将对象属性" + ognlString +":" + e.getLocalizedMessage(),e);
			}
		}
		if(ognl.length - 1 > index){
			result = executeOgnl(result,ognl,++index,args,argsIndex);
		}
		return result;
	}
	
//	public static void main(String[] args) throws OgnlException {
//		HashMap<String, Object> obj = new HashMap<String, Object>();
//		obj.put("date1", new StringBuilder());
//		String ognlString="#date1->@insert(int,java.lang.String)";
//		OgnlContext.execute(obj, ognlString, 0,"这是一条数据");
//		ognlString="#date1->@toString()";
//		Object execute = OgnlContext.execute(obj, ognlString);
//		System.out.println(execute);
//	}
}
