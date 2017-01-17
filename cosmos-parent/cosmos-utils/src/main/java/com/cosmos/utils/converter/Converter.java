package com.cosmos.utils.converter;

public interface Converter<F, T> {
	
	public T to(F obj) throws ConvertException;

	public F from(T obj) throws ConvertException;
	
	public void initialize(Object obj) throws ConvertException;
}
