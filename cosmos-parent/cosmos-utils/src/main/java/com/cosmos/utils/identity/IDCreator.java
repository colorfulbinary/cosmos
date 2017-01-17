package com.cosmos.utils.identity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class IDCreator {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public synchronized static String getDateString(){
		String result = format.format(new Date());
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getUUIDString(){
		return UUID.randomUUID().toString();
	}
	
	public static void main(String[] args) {
		System.out.println(IDCreator.getDateString());
	}
}
