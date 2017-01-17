package com.cosmos.utils.text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat ymdhFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String getDataStringByYmd(Date date){
		return ymdFormat.format(date);
	}
	
	public static String getDataStringByYmdhms(Date date){
		return ymdhFormat.format(date);
	}
	
	public static Date parseDataStringByYmd(String date) throws ParseException{
		return ymdhFormat.parse(date);
	}
	
	public static Date parseDataStringByYmdhms(String date) throws ParseException{
		return ymdhFormat.parse(date);
	}
}
