package com.cosmos.utils.converter;

import java.util.Date;

import com.cosmos.utils.converter.impls.StringAndDate;
import com.cosmos.utils.converter.impls.StringAndIntegerConverter;

public class DefaultConverterFactory extends ConverterFactory{
	
	private boolean isBuffered;
	
	public DefaultConverterFactory(){
		this(false);
	}
	
	public DefaultConverterFactory(boolean isBuffered){
		this.isBuffered = isBuffered;
	}
	
	@Override
	public CItem[] initialize() {
		CItem[] result = null;
		try {
			result =  new CItem[]{
					new CItem(String.class, Integer.class, StringAndIntegerConverter.class,this.isBuffered),
					new CItem(String.class, Date.class, StringAndDate.class,this.isBuffered)
			};
		} catch (ConvertException e) {
			e.printStackTrace();
		}
		return result;
	}
	
//	public static void main(String[] args) throws ConvertException {
//		ConverterFactory cf = new DefaultConverterFactory();
//		Date convert2 = cf.convert("2016-11-23 17:43:57", Date.class, "yyyy-MM-dd HH:mm:ss");
//		String convert = cf.convert(convert2, String.class,"yyyy-MM-dd HH:mm:ss");
//		System.out.println(convert);
//	}
}
