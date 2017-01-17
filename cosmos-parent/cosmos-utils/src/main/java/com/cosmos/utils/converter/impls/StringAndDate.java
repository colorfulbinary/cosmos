package com.cosmos.utils.converter.impls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cosmos.utils.converter.ConvertException;
import com.cosmos.utils.converter.Converter;

public class StringAndDate implements Converter<String, Date>{
	
	private static ThreadLocal<SimpleDateFormat> format = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		};
	};
	
	@Override
	public Date to(String obj) throws ConvertException {
		Date parse;
		try {
			parse = format.get().parse(obj);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new ConvertException("String转Date失败");
		}
		return parse;
	}

	@Override
	public String from(Date obj) throws ConvertException {
		return  format.get().format(obj);
	}

	@Override
	public void initialize(Object obj) throws ConvertException {
		format.set(new SimpleDateFormat(obj.toString()));
	}
	
}
