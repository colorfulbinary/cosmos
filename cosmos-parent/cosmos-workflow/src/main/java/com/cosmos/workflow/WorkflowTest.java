package com.cosmos.workflow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowInformation;
import com.cosmos.workflow.runtime.factory.sequence.SequenceXmlFileWorkflowInformation;

public class WorkflowTest {
	public static void init() throws WorkflowRuntimeException {
		InputStream c2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("ssh.xml");
		SequenceXmlFileWorkflowInformation if1 = new SequenceXmlFileWorkflowInformation("tag1", c2);
		WorkflowInformation<SequenceActivity>[] ifs1 = new SequenceXmlFileWorkflowInformation[]{if1};
		WorkflowInitializer.initialize(ifs1, null);
		//WorkflowInitializer.enableClearSchedule(1, 1000);
	}
	
	public static void main(String[] args) throws Exception {
		init();
		
//		String[] unames = {"zhangsan","lisi","wangwu"};
		
		Map<String, String> d = new HashMap<String, String>();
		d.put("name", "zhangsan");
		d.put("age", "29");
		
		Map<Object, Object> sd = new HashMap<Object, Object>();
		sd.put("value1", "zhangsan".getBytes());
		sd.put("value2", "28");
		sd.put("value3", d);
		sd.put("value", new String[]{"zhangruyi","wangtao","wudongxing"});
//		sd.put("groupName", "gptest");
//		sd.put("groupId", "76543");
//		sd.put("basePath", "<root><image id='456'>123</image></root>");
		try {
			WorkflowInovker.invoke("redis-test", sd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("结束");
	}
}
