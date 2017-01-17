package com.cosmos.utils.ognl;

/*
 * Name      : MapOgnl
 * Creator   : rouies
 * Function  : Map对象的OGNL功能声明
 * Date      : 2016-1-18
 */
public interface MapOgnl {
	/*
	 * MethodName : getSubItemByOgnl
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据OGNL表达式获取子集
	 * Arguments  : String ognl -> OGNL表达式
	 * Return     : MapOgnl
	 */
	public MapOgnl getSubItemByOgnl(String ognl) throws OgnlException;
	
	/*
	 * MethodName : getStringValueByOgnl
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据OGNL表达式获取字符串
	 * Arguments  : String ognl -> OGNL表达式
	 * Return     : String
	 */
	public String getStringValueByOgnl(String ognl) throws OgnlException;
	
	/*
	 * MethodName : getNumberValueByOgnl
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据OGNL表达式获取数字
	 * Arguments  : String ognl -> OGNL表达式
	 * Return     : Number
	 */
	public Number getNumberValueByOgnl(String ognl) throws OgnlException;
	
	/*
	 * MethodName : setStringValueByOgnl
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据OGNL表达式设置字符串值
	 * Arguments  : String ognl  -> OGNL表达式
	 *              String value -> 要设置的值
	 * Return     : void
	 */
	public void setStringValueByOgnl(String ognl,String value) throws OgnlException;
	
	/*
	 * MethodName : setNumberValueByOgnl
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据OGNL表达式设置数字值
	 * Arguments  : String ognl  -> OGNL表达式
	 *              Number value -> 要设置的值
	 * Return     : void
	 */
	public void setNumberValueByOgnl(String ognl,Number value) throws OgnlException;
}
