package com.cosmos.utils.converter;

import java.util.Arrays;
import java.util.HashSet;

public abstract class ConverterFactory {
		
	private HashSet<CItem> cls;
	
	public ConverterFactory(){
		CItem[] initList = this.initialize();
		cls = new HashSet<CItem>(Arrays.asList(initList));
	}
	
	protected abstract CItem[] initialize();
	
	@SuppressWarnings("unchecked")
	public <I,O> O convert(I source,Class<O> to,Object initObject) throws ConvertException{
		Class<? extends Object> sclazz = source.getClass();
		O result = null;
		if(sclazz != to){
			CItem c = new CItem(sclazz, to);
			for (CItem cItem : cls) {
				if(cItem.equals(c)){
					result = cItem.convert(source, to,initObject);
					break;
				}
			}
			if(result == null){
				throw new ConvertException("没有找到转换器");
			}
		} else {
			result = (O) source;
		}
		
		return result;
	}
	
	public <I,O> O convert(I source,Class<O> to) throws ConvertException{
		return convert(source,to,null);
	}
	
	public static void main(String[] args) throws ConvertException {
		ConverterFactory cf = new DefaultConverterFactory();
		String obj = "111123123";
		Integer convert = cf.convert(obj, Integer.class);
		System.out.println(convert);
	}
}
