package com.cosmos.utils.text;

public class StringUtils {
	
	public static boolean isEmptyOrNull(Object str){
		boolean result = false;
		if(str == null){
			result = true;
		} else if(str instanceof String && str.toString().trim().equals("")){
			result = true;
		}
		return result;
	}
	
	public static String join(String[] str,String splitChar){
		String result = null;
		if(str != null && str.length != 0){
			StringBuilder sb = new StringBuilder(50);
			for (int i = 0 , len= str.length; i < len; i++) {
				if(i != 0){
					sb.append(splitChar);
				}
				sb.append(str[i]);
			}
			result = sb.toString();
		}
		return  result;
	}

}
