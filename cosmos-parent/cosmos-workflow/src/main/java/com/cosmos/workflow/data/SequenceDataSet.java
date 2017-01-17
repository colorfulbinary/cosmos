package com.cosmos.workflow.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.cosmos.utils.converter.ConverterFactory;
import com.cosmos.workflow.activities.sequence.action.convertor.WorkflowConverterFactory;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class SequenceDataSet implements ISequenceLogicData{
	
	private boolean isSuccess;
	
	private boolean isBreak;
	
	private int index;
	
	private boolean isContinue;
	
	private Map<Object, Object> data;

	private Exception exception;
	
	public SequenceDataSet(Map<Object, Object> data){
		this.data = data;
	}
	
	public boolean isBreak() {
		return isBreak;
	}

	public void setBreak(boolean isBreak) {
		this.isBreak = isBreak;
	}

	public boolean isContinue() {
		return isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int size() {
		return this.data.size();
	}

	public boolean isEmpty() {
		return this.data.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.data.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.data.containsValue(value);
	}

	public Object get(Object key) {
		return this.data.get(key);
	}
	
	@Override
	public <T> T getValue(Object key, Class<T> clazz) throws Exception {
		T result = null;
		ConverterFactory cf = WorkflowConverterFactory.getConverterFactory();
		Object source = this.data.get(key);
		if(source != null){
			result = cf.convert(source, clazz);
		}
		return result;
	}

	public Object put(Object key, Object value) {
		return this.data.put(key, value);
	}

	public Object remove(Object key) {
		return this.data.remove(key);
	}

	public void putAll(Map<? extends Object, ? extends Object> m) {
		this.data.putAll(m);
	}

	public void clear() {
		this.data.clear();
	}

	public Set<Object> keySet() {
		return this.data.keySet();
	}

	public Collection<Object> values() {
		return this.data.values();
	}

	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		return this.data.entrySet();
	}

	

	@Override
	public Object[] getValueByNames(String... names) {
		Object[] result = new Object[names.length];
		for(int i=0,len = names.length ; i< len ; i++){
			result[i] = this.get(names[i]);
		}
		return result;
	}

	@Override
	public String getBoundValue(String name) {
		String result = null;
		if(name.startsWith("{")){
			Object target = data.get(name.substring(1,name.length() - 1));
			if(target != null ){
				result = target.toString();
			}
		} else {
			result = name;
		}
		return result;
	}
}
