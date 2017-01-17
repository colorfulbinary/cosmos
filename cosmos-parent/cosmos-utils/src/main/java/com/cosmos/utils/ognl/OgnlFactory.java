package com.cosmos.utils.ognl;

import java.util.Map;

/*
 * Name      : OgnlFactory
 * Creator   : rouies
 * Function  : OGNL操作对象工厂类
 * Date      : 2016-1-18
 */
public class OgnlFactory {
	
	/*
	 * MethodName : createMapOgnl
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 获取MapOgnl操作接口的实现
	 * Arguments  : Map<Object, Object> map -> 要包装的Map实现
	 * Return     : MapOgnl
	 */
	public static MapOgnl createMapOgnl(Map<Object, Object> map){
		return new OgnlMap(map);
	}
	
	/*
	 * MethodName : MapOgnlExt
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 获取MapOgnl扩展操作接口的实现
	 * Arguments  : Map<Object, Object> map -> 要包装的Map实现
	 * Return     : MapOgnlExt
	 */
	public static MapOgnlExt createMapOgnlExt(Map<Object, Object> map){
		return new OgnlMap(map);
	}
}
