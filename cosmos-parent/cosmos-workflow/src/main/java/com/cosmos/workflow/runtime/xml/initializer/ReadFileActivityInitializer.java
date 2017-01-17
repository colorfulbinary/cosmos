package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.io.ReadFileActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class ReadFileActivityInitializer  implements IXmlInitializer<ReadFileActivity>{
	@Override
	public void init(ReadFileActivity activity, Element item)
			throws WorkflowRuntimeException {
		String out = item.attributeValue("out");
		if(StringUtils.isEmptyOrNull(out)){
			throw new WorkflowRuntimeException("必须指定一个输出位置!");
		}
		activity.setOut(out);
		String id = item.attributeValue("id");
		String src = item.attributeValue("src");
		if(src == null || src.trim().equals("")){
			throw new WorkflowRuntimeException("必须指定一个要读的文件");
		}
		activity.setActivityId(id);
		activity.setFilePath(src);
	}

}
