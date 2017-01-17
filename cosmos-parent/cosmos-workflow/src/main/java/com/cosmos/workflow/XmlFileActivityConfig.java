package com.cosmos.workflow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class XmlFileActivityConfig {
	
	private static volatile XmlFileActivityConfig instance = null;
	
	public static XmlFileActivityConfig getInstance() {
		return XmlFileActivityConfig.instance;
	}
	
	static void setInstance(InputStream...ins) throws WorkflowRuntimeException{
		if(XmlFileActivityConfig.instance == null){
			synchronized (XmlFileActivityConfig.class) {
				if(XmlFileActivityConfig.instance == null){
					instance = new XmlFileActivityConfig(ins);
				}
			}
		} 
	}
	
	private XmlFileActivityConfig(InputStream...ins) throws WorkflowRuntimeException{
		this.initialize(ins);
	};
	
	private Map<String, Config> configs = new HashMap<String, Config>(32);
	
	public class Config {
		
		private Config(){};
		
		private String classPath;
		
		private String initializerClassPath;

		public String getClassPath() {
			return classPath;
		}

		private void setClassPath(String classPath) {
			this.classPath = classPath;
		}

		public String getInitializerClassPath() {
			return initializerClassPath;
		}

		private void setInitializerClassPath(String initializerClassPath) {
			this.initializerClassPath = initializerClassPath;
		}
	}
	
	private void initialize(InputStream...ins) throws WorkflowRuntimeException{
		SAXReader reader = new SAXReader(new DocumentFactory());
		Map<String, String> namespances = new HashMap<String, String>();  
		namespances.put("wfi","http://rouies.com/workflow-activity-config");
		reader.getDocumentFactory().setXPathNamespaceURIs(namespances);
		try {
			for (InputStream in : ins) {
				Document doc = reader.read(in);
				@SuppressWarnings("unchecked")
				List<Element> selectNodes = doc.selectNodes("/wfi:configuration/wfi:activity");
				for (Element item : selectNodes) {
					Config conf = new Config();
					conf.setClassPath(item.attributeValue("class"));
					conf.setInitializerClassPath(item.attributeValue("initializer"));
					configs.put(item.attributeValue("name"), conf);
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new WorkflowRuntimeException("读取节点配置失败:" + e.getMessage());
		} finally {
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Activity> T getActivityInstance(Element item,
			Class<T> targetClass) throws WorkflowRuntimeException {
		String name = item.getName();
		T result = null;
		try {
			if (this.configs.containsKey(name)) {
				Config conf = this.configs.get(name);
				String classPath = conf.getClassPath();
				String initializerClassPath = conf.getInitializerClassPath();
				try {
					Class<?> clazz = Class.forName(classPath);
					result = (T) clazz.newInstance();
				} catch (Exception e) {
					throw new WorkflowRuntimeException("创建Activity实例" + classPath+ "失败:" + e.getMessage());
				}
				if (initializerClassPath != null && !"".equals(initializerClassPath)) {
					try {
						Class<?> initClazz = Class.forName(initializerClassPath);
						Object initInstance = initClazz.newInstance();
						if (initInstance instanceof IXmlInitializer) {
							((IXmlInitializer<Activity>) initInstance).init(result,item);
						} else {
							throw new WorkflowRuntimeException("创建Activity初始化器"+ initializerClassPath + "失败:无效的初始化器!");
						}
					} catch (Exception e) {
						throw new WorkflowRuntimeException("创建Activity初始化器"
								+ initializerClassPath + "失败:" + e.getMessage());
					}
				}
			}
		} catch (WorkflowRuntimeException e) {
			throw e;
		}
		return result;
	}
}
