package com.cosmos.utils.converter.impls;

import com.cosmos.utils.converter.ConvertException;
import com.cosmos.utils.converter.Converter;

public class StringAndIntegerConverter implements Converter<String, Integer>{

	@Override
	public Integer to(String obj) throws ConvertException {
		Integer result = null;
		try {
			if(obj != null){
				result = new Integer(obj);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ConvertException("无法由String:" + obj + "转换为Integer");
		}
		return result;
	}

	@Override
	public String from(Integer obj) throws ConvertException {
		return obj == null ? null :obj.toString();
	}

	@Override
	public void initialize(Object obj) throws ConvertException {
		
	}

	
}
