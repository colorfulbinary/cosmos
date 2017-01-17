package com.cosmos.utils.text;

public class XmlUtils {
	public static String replaceXPathStringFromNamespace(String xpath,String pix){
		return xpath.replaceAll("/(\\w)", "/"+ pix + ":$1").replaceAll("^(\\w)", pix + ":$1"); 
	}
}
