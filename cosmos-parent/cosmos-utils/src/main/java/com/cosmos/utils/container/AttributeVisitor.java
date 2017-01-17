package com.cosmos.utils.container;

public interface AttributeVisitor {
	
	public Object getAttribute(String key);
	
	public void setArribute(String key,Object value);
}
