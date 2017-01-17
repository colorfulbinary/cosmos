package com.cosmos.utils.json;

/*
 * ClassName : JsonStringConvert
 * Creator   : rouies
 * Function  : JSON字符串转义字符实现
 * Date      : 2016-1-18
 */
public class JsonStringConvert {
	
	private static String[] srcChars = new String[]{
		"&syh;"
	};
	
	private static String[] targetChars = new String[]{
		"\""
	};
	
	/*
	 * MethodName : convertTo
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 将转义字符替换回原字符
	 * Arguments  : String str -> 内容字符串
	 * Return     : String
	 */
	public static String convertTo(String str){
		for (int i = 0,len = srcChars.length; i < len; i++) {
			str = str.replaceAll(srcChars[i], targetChars[i]);
		}
		return str;
	}
	
	/*
	 * MethodName : fromConvert
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 将原字符替换为转义字符
	 * Arguments  : String jsonString -> 内容字符串
	 * Return     : String
	 */
	public static String fromConvert(String str){
		for (int i = 0,len = srcChars.length; i < len; i++) {
			str = str.replaceAll(targetChars[i],srcChars[i]);
		}
		return str;
	}
}
