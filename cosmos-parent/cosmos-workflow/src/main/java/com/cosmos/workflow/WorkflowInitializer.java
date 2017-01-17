package com.cosmos.workflow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cosmos.utils.pools.SingletonObjectPools;
import com.cosmos.utils.reflect.ClassReflectUtils;
import com.cosmos.utils.reflect.ReflectException;
import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.activities.state.StateActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowFactory;
import com.cosmos.workflow.runtime.factory.WorkflowInformation;

public class WorkflowInitializer {
	
	private static boolean isFirst = true;
	
	private static ScheduledExecutorService schedule;
	
	private static String sequenceFactoryPath;
	
	private static String stateFactoryPath;
	
	public static void initialize(WorkflowInformation<SequenceActivity>[] se_activities,WorkflowInformation<StateActivity>[] st_activities) throws WorkflowRuntimeException{
		InputStream c0 = Thread.currentThread().getContextClassLoader().getResourceAsStream("workflow-initializer.xml");
		InputStream c1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("default-workflow-activity-config.xml");
		WorkflowInitializer.initialize(c0, new InputStream[]{c1}, se_activities, st_activities);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized static void initialize(InputStream initializer,InputStream[] actConfigs,WorkflowInformation<SequenceActivity>[] se_activities,WorkflowInformation<StateActivity>[] st_activities) throws WorkflowRuntimeException{
		if(!isFirst){
			throw new WorkflowRuntimeException("初始化操作只能执行一次");
		}
		isFirst = false;
		SAXReader reader = new SAXReader(new DocumentFactory());
		Map<String, String> namespances = new HashMap<String, String>();  
		namespances.put("wfi","http://rouies.com/workflow-initializer-config");
		reader.getDocumentFactory().setXPathNamespaceURIs(namespances);
		Document doc;
		try {
			doc = reader.read(initializer);
		} catch (DocumentException e) {
			throw new WorkflowRuntimeException("无法读取xml配置:" + e.getMessage());
		}
		Node seq = doc.selectSingleNode("/wfi:configuration/wfi:factory-config/wfi:factory-item[@name='SEQUENCE_FACTORY']");
		if(seq == null){
			throw new WorkflowRuntimeException("找不到顺序流配置信息!");
		}
		String seqClazz = ((Element)seq).attributeValue("class");
		Object seqObj = null;
		try {
			seqObj = ClassReflectUtils.getInstance(seqClazz);
		} catch (ReflectException e) {
			throw new WorkflowRuntimeException("无法获取工厂实例:" + e.getMessage());
		}
		WorkflowFactory<SequenceActivity> efactory;
		if(seqObj instanceof WorkflowFactory){
			try {
				efactory = (WorkflowFactory<SequenceActivity>)seqObj;
			} catch (Exception e) {
				throw new WorkflowRuntimeException("转换工厂失败:" + e.getMessage());
			}
		} else {
			throw new WorkflowRuntimeException("转换工厂失败:不是工厂类型");
		}
		Node sta = doc.selectSingleNode("/wfi:configuration/wfi:factory-config/wfi:factory-item[@name='STATE_FACTORY']");
		if(sta == null){
			throw new WorkflowRuntimeException("找不到状态流配置信息!");
		}
		String staClazz = ((Element)sta).attributeValue("class");
		Object staObj = null;
		try {
			staObj = ClassReflectUtils.getInstance(staClazz);
		} catch (ReflectException e) {
			throw new WorkflowRuntimeException("无法获取工厂实例:" + e.getMessage());
		}
		WorkflowFactory<StateActivity> tfactory;
		if(staObj instanceof WorkflowFactory){
			try {
				tfactory = (WorkflowFactory<StateActivity>)staObj;
			} catch (Exception e) {
				throw new WorkflowRuntimeException("转换工厂失败:" + e.getMessage());
			}
		} else {
			throw new WorkflowRuntimeException("转换工厂失败:不是工厂类型");
		}
		sequenceFactoryPath = seqClazz;
		stateFactoryPath = staClazz;
		SingletonObjectPools.registerSingletonObject(seqObj);
		SingletonObjectPools.registerSingletonObject(staObj);
		XmlFileActivityConfig.setInstance(actConfigs);
		if(se_activities != null && se_activities.length > 0){
			efactory.register(se_activities);
		}
		if(st_activities != null && st_activities.length > 0){
			tfactory.register(st_activities);
		}
		
	}
	
	
	
	public static String getSequenceFactoryPath() {
		return sequenceFactoryPath;
	}
	public static String getStateFactoryPath() {
		return stateFactoryPath;
	}
	
	public synchronized static void enableClearSchedule(int tsize,int period) throws WorkflowRuntimeException{
		if(isFirst){
			throw new WorkflowRuntimeException("必须先进行初始化");
		}
		if(schedule == null){
			schedule = Executors.newScheduledThreadPool(tsize);
			schedule.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					
					WorkflowFactory<?> factory;
					try {
						factory = WorkflowFactory.getWorkflowFactory(sequenceFactoryPath);
						factory.clear();
					} catch (WorkflowRuntimeException e) {
						e.printStackTrace();
					}
				}
			}, period, period, TimeUnit.MILLISECONDS);
		} else {
			throw new WorkflowRuntimeException("清理程序只能初始化一次");
		}
	}
	
	public synchronized static void disableClearSchedule(){
		if(schedule != null){
			schedule.shutdown();
		}
	}
}
