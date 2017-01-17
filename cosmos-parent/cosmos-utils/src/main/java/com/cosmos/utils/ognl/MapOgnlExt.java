package com.cosmos.utils.ognl;

/*
 * Name      : MapOgnlExt
 * Creator   : rouies
 * Function  : Map对象的OGNL扩展功能声明
 * Date      : 2016-1-18
 */
public interface MapOgnlExt extends MapOgnl{
	
	/*
	 * MethodName : getObjectByOgnl
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据OGNL表达式获取对象
	 * Arguments  : String ognl -> OGNL表达式
	 * Return     : Object
	 */
	public Object getObjectByOgnl(String ognl) throws OgnlException;
	
	/*
	 * MethodName : setObjectByOgnl
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据OGNL表达式设置值
	 * Arguments  : String ognl  -> OGNL表达式
	 *              Object value -> 要设置的值
	 * Return     : void
	 */
	public void setObjectByOgnl(String ognl,Object value) throws OgnlException;
}
