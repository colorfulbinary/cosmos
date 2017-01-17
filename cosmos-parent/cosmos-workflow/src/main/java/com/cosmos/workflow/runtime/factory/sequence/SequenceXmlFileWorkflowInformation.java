package com.cosmos.workflow.runtime.factory.sequence;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cosmos.workflow.XmlFileActivityConfig;
import com.cosmos.workflow.activities.sequence.SQActivity;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.logic.CompensateActivity;
import com.cosmos.workflow.activities.sequence.logic.FinalActivity;
import com.cosmos.workflow.activities.sequence.logic.LogicActivity;
import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowInformation;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;

public class SequenceXmlFileWorkflowInformation implements WorkflowInformation<SequenceActivity>{
	
	private Document doc;
	
	private String tag;
	
	private String workflowId;
	
	@Override
	public String getWorkflowId() {
		return this.workflowId;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	private static void loadActivities(LogicActivity act, Element ele) throws WorkflowRuntimeException {
		@SuppressWarnings("unchecked")
		Iterator<Element> iterator = ele.elementIterator();
		while (iterator.hasNext()) {
			Element item = iterator.next();
			if(item.attributeValue("is-parse") != null){
				continue;
			}
			SQActivity itemActivity = XmlFileActivityConfig.getInstance().getActivityInstance(item,SQActivity.class);
			if (itemActivity instanceof LogicActivity){
				loadActivities((LogicActivity) itemActivity, item);
			} else if(itemActivity instanceof ActionActivity){
				Node comp = null;
				if((comp = item.element("compensation")) != null){
					Element citem = (Element) comp;
					CompensateActivity ca = new CompensateActivity();
					loadActivities(ca, citem);
					((ActionActivity)itemActivity).setCompensation(ca);
				}
				Node afinal = null;
				if((afinal = item.element("finally")) != null){
					Element citem = (Element) afinal;
					FinalActivity fa = new FinalActivity();
					loadActivities(fa, citem);
					((ActionActivity)itemActivity).setFinal(fa);
				}
			}
			itemActivity.setActivityId(item.attributeValue("id"));
			act.appendChild(itemActivity);
		}
	}
	
	public SequenceXmlFileWorkflowInformation(String tag,InputStream in) throws WorkflowRuntimeException {
		this.tag = tag;
		SAXReader reader = new SAXReader(new DocumentFactory());
		Map<String, String> namespances = new HashMap<String, String>();  
		namespances.put("wf","http://rouies.com/workflow");
		reader.getDocumentFactory().setXPathNamespaceURIs(namespances);
		try {
			this.doc = reader.read(in);
		} catch (DocumentException e) {
			throw new WorkflowRuntimeException("无法读取xml配置:" + e.getMessage());
		}
		Element root = this.doc.getRootElement();
		this.workflowId = root.attributeValue("id");
	}

	@Override
	public WorkflowInstance<SequenceActivity> getItemById(String key) throws WorkflowRuntimeException {
		SequenceWorkflowInstance result = null;
		Node node = this.doc.selectSingleNode("/wf:workflow/wf:sequence[@id='" + key + "']");
		if(node != null && node instanceof Element){
			result = new SequenceWorkflowInstance();
			Element item = (Element)node;
			//SequenceActivity sa = new SequenceActivity();
			SequenceActivity sa = XmlFileActivityConfig.getInstance().getActivityInstance(item,SequenceActivity.class);
			sa.setActivityId(key);
			loadActivities(sa, item);
			result.setRoot(sa);
			result.setTime(System.currentTimeMillis());
			result.setTimeout(sa.getTimeout());
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public WorkflowInstance<SequenceActivity>[] getItems(boolean isLoad) throws WorkflowRuntimeException {
		List<Element> seqs = doc.selectNodes("/wf:workflow/wf:sequence");
		WorkflowInstance<SequenceActivity>[] result = new WorkflowInstance[seqs.size()];
		int index = 0;
		for (Element seq : seqs) {
			String key = seq.attributeValue("id");
			SequenceWorkflowInstance instance = new SequenceWorkflowInstance();
			if(isLoad){
				SequenceActivity sa = XmlFileActivityConfig.getInstance().getActivityInstance(seq,SequenceActivity.class);
				sa.setActivityId(key);
				loadActivities(sa, seq);
				instance.setRoot(sa);
				instance.setTimeout(sa.getTimeout());
			}
			instance.setTime(System.currentTimeMillis());
			instance.setKey(key);
			result[index++] = instance;
		}
		return result;
	}	
	
//	public static void main(String[] args) throws WorkflowRuntimeException, WorkflowException {
//		InputStream c1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("default-workflow-activity-config.xml");
//		XmlFileActivityConfig.getInstance(c1);
//		InputStream c2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("demo.xml");
//		SequenceXmlFileWorkflowInformation info = SequenceXmlFileWorkflowInformation.getInstance("test", c2);
//		SequenceActivity root = info.getItemById("sequence1").getExecutableActivity();
//		root.execute(new SequenceDataSet(new HashMap<Object, Object>()));
//	}

}
