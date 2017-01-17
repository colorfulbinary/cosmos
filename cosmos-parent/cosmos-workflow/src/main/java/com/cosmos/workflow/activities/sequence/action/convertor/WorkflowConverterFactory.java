package com.cosmos.workflow.activities.sequence.action.convertor;

import com.cosmos.utils.converter.CItem;
import com.cosmos.utils.converter.ConverterFactory;
import com.cosmos.utils.converter.DefaultConverterFactory;

public class WorkflowConverterFactory extends DefaultConverterFactory{
	
	private static WorkflowConverterFactory instance = new WorkflowConverterFactory();
	
	private WorkflowConverterFactory(){
		
	}
	
	public static ConverterFactory getConverterFactory(){
		return instance;
	}
	
	@Override
	public CItem[] initialize() {
		CItem[] sList = super.initialize();
		return sList;
	}

}
