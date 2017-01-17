package com.cosmos.workflow.activities.sequence.action.convertor;

import com.cosmos.utils.converter.ConverterFactory;
import com.cosmos.utils.reflect.ClassReflectUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class TypeConvertActivity extends ActionActivity{
	
	private static final long serialVersionUID = 421817772327020637L;

	private String targetType;
	
	private String initValue;
	
	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getInitValue() {
		return initValue;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}
	
	@Override
	public void release() {
		targetType = null;
		initValue = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		String source = this.in;
		if(source!=null && !source.trim().equals("")){
			Object instance = data.get(source);
			if(instance!=null){
				ConverterFactory cf = WorkflowConverterFactory.getConverterFactory();
				Object result;
				try {
					Class<?> targetClass = ClassReflectUtils.getClassFromString(targetType);
					result = cf.convert(instance, targetClass,this.initValue);
				} catch (Exception e) {
					throw new WorkflowException(this, "转换失败:" + e.getMessage(),e);
					
				} 
				data.put(this.out, result);
			} else {
				throw new WorkflowException(this, "没有找到指定in中的实例!");
			}
		} else {
			throw new WorkflowException(this, "没有找到指定in中的实例!");
		}
	}

}
