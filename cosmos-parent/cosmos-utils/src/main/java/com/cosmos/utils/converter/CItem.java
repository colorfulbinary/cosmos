package com.cosmos.utils.converter;

public class CItem {
	
	private Class<?> from;
	
	private Class<?> to;
	
	private Converter<?, ?> converter;
	
	private Class<? extends Converter<?,?>> clazz;
	
	CItem(Class<?> from,Class<?> to){
		this.from = from;
		this.to = to;
	}
	
	public CItem(Class<?> from,Class<?> to,Class<? extends Converter<?,?>> clazz) throws ConvertException{
		this(from,to,clazz,false);
	}
	
	public CItem(Class<?> from,Class<?> to,Class<? extends Converter<?,?>> clazz,boolean isBuffered) throws ConvertException{
		this.from = from;
		this.to = to;
		this.clazz = clazz;
		if(isBuffered){
			try {
				this.converter = clazz.newInstance();
			} catch (Exception e) {
				throw new ConvertException("创建转换器失败:" + e.getMessage(),e);
			} 
		}
	}
		
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if(obj instanceof CItem){
			CItem c = (CItem) obj;
			result = (this.from == c.from && this.to == c.to) || (this.from == c.to && this.to == c.from);
		}
		return result;
	}
	
	ConvertType checkObject(Class<?> from,Class<?> to) {
		ConvertType result = ConvertType.NOT_FOUND;
		if(this.from == from && this.to == to){
			result = ConvertType.TO;
		} else if(this.from == to && this.to == from){
			result = ConvertType.FROM;
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	<I,O> O convert(I source,Class<O> to,Object initObject) throws ConvertException{
		Class<?> from = source.getClass();
		ConvertType checkObject = this.checkObject(from, to);
		if(checkObject == ConvertType.NOT_FOUND) {
			throw new ConvertException("转换器类型无法匹配,转换失败!");
		}
		Converter conv = this.converter;
		if(conv == null){
			try {
				conv = this.clazz.newInstance();
			} catch (Exception e) {
				throw new ConvertException("初始化转换器错误:" + e.getMessage(),e);
			}
		}
		O result = null;
		if(initObject != null){
			conv.initialize(initObject);
		}
		if(checkObject == ConvertType.FROM){
			result = (O) conv.from(source);
		} else if(checkObject == ConvertType.TO){
			result = (O) conv.to(source);
		} 
		return result;
	}
	
	public <I,O> O convert(I source,Class<O> to) throws ConvertException{
		return convert(source,to,null);
	}
}
