package com.cosmos.utils.ognl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/*
 * Name      : OgnlMap
 * Creator   : rouies
 * Function  : MapOgnl与MapOgnlExt的包装实现类
 * Date      : 2016-1-18
 */
public class OgnlMap implements Map<Object, Object>,MapOgnlExt{
	
	private Map<Object, Object> map = null;
	
	public OgnlMap(Map<Object, Object> in){
		this.map = in;
	}
	
	/***********************Map包装接口默认实现-begin******************************/
	public int size() {
		return this.map.size();
	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	public Object get(Object key) {
		return this.map.get(key);
	}

	public Object put(Object key, Object value) {
		return this.map.put(key, value);
	}

	public Object remove(Object key) {
		return this.map.remove(key);
	}

	public void putAll(Map<? extends Object, ? extends Object> m) {
		this.map.putAll(m);
	}

	public void clear() {
		this.map.clear();
	}

	public Set<Object> keySet() {
		return this.map.keySet();
	}

	public Collection<Object> values() {
		return this.map.values();
	}

	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		return this.map.entrySet();
	}
	
	public Map<Object, Object> getNormalMap(){
		return this.map;
	}
	/***********************Map包装接口默认实现-end******************************/
	
	/***********************MapOgnlExt实现-begin******************************/
	@SuppressWarnings("unchecked")
	public void setObjectByOgnl(String ognl,Object value) throws OgnlException{
		Map<Object, Object> context = this.map;
		StringBuilder path = new StringBuilder(ognl.length());
		path.append("#");
		String[] fields = ognl.split("\\.");
		if(!fields[0].equals("#")){
			throw new OgnlException("root not found!");
		}
		for (int i = 1 ,len = fields.length; i < len; i++) {
			path.append(".").append(fields[i]);
			if(context.containsKey(fields[i])){
				if(i == len - 1){
					context.put(fields[i], value);
				} else {
					Object item = context.get(fields[i]);
					if(item!=null && item instanceof Map<?, ?>){
						context = (Map<Object, Object>) item;
					} else {
						throw new OgnlException("field type error:" + path.substring(1));
					}
				}
			} else {
				throw new OgnlException("field not found:" + path.substring(1));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object getObjectByOgnl(String ognl) throws OgnlException{
		Object result = null;
		Map<Object, Object> context = this.map;
		StringBuilder path = new StringBuilder(ognl.length());
		path.append("#");
		String[] fields = ognl.split("\\.");
		if(!fields[0].equals("#")){
			throw new OgnlException("root not found!");
		}
		for (int i = 1 ,len = fields.length; i < len; i++) {
			path.append(".").append(fields[i]);
			if(context.containsKey(fields[i])){
				if(i == len - 1){
					result = context.get(fields[i]);
				} else {
					Object item = context.get(fields[i]);
					if(item!=null && item instanceof Map<?, ?>){
						context = (Map<Object, Object>) item;
					} else {
						throw new OgnlException("field type error:" + path);
					}
				}
			} else {
				throw new OgnlException("field not found:" + path);
			}
		}
		return result;
	}
	/***********************MapOgnlExt实现-end******************************/
	
	/***********************MapOgnl实现-begin******************************/
	@SuppressWarnings("unchecked")
	public OgnlMap getSubItemByOgnl(String ognl) throws OgnlException{
		OgnlMap result = null;
		Object item = this.getObjectByOgnl(ognl);
		if(item!=null && item instanceof Map<?, ?>){
			Map<Object, Object> m = (Map<Object, Object>) item;
			result = new OgnlMap(m);
		} else {
			throw new OgnlException("field is not map:" + ognl);
		}
		return  result;
	}
	
	public String getStringValueByOgnl(String ognl) throws OgnlException{
		String result = null;
		Object item = this.getObjectByOgnl(ognl);
		if(item instanceof String){
			result = item.toString();
		} else if(item != null){
			throw new OgnlException("field is not a string:" + ognl);
		}
		return  result;
	}
	
	public Number getNumberValueByOgnl(String ognl) throws OgnlException{
		Number result = null;
		Object item = this.getObjectByOgnl(ognl);
		if(item instanceof Number){
			result = (Number) item;
		} else if(item != null){
			throw new OgnlException("field is not a number:" + ognl);
		} else {
			throw new OgnlException("field is null:" + ognl);
		}
		return  result;
	}
	
	public void setStringValueByOgnl(String ognl, String value)
			throws OgnlException {
		this.setObjectByOgnl(ognl, value);
	}

	public void setNumberValueByOgnl(String ognl, Number value)
			throws OgnlException {
		if(value == null){
			throw new OgnlException("field is null:" + ognl);
		}
		Object res = null;
		double val = value.doubleValue();
		if(val == Math.floor(val)){
			res = value.longValue();
		} else {
			res = val;
		}
		this.setObjectByOgnl(ognl, res);
		
	}
	/***********************MapOgnl实现-end******************************/
	
//	public static void main(String[] args) throws OgnlException {
//		Map<Object, Object> map1 = new HashMap<Object, Object>();
//		Map<Object, Object> map2 = new HashMap<Object, Object>();
//		Map<Object, Object> map3 = new HashMap<Object, Object>();
//		map1.put("name", "zhangsan");
//		map1.put("age", 55);
//		map1.put("child", map2);
//		map2.put("name", "zhangxiaosan");
//		map2.put("age", 33);
//		map2.put("child", map3);
//		map3.put("name", "xiaoxiaozhang");
//		map3.put("age", 11);
//		
//		MapOgnl mp = OgnlFactory.createMapOgnl(map1);
//		mp.setNumberValueByOgnl("#.child.child.age", 100.00);
//		Number v = mp.getNumberValueByOgnl("#.child.child.age");
//		System.out.println(v);
//	}
}
