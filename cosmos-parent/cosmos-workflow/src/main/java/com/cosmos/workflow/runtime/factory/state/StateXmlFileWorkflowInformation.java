package com.cosmos.workflow.runtime.factory.state;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cosmos.utils.identity.IDCreator;
import com.cosmos.workflow.XmlFileActivityConfig;
import com.cosmos.workflow.activities.state.StateActivity;
import com.cosmos.workflow.activities.state.TargetStateEntity;
import com.cosmos.workflow.logic.CompileException;
import com.cosmos.workflow.logic.ILogicSource;
import com.cosmos.workflow.logic.Logic;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowInformation;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;

public class StateXmlFileWorkflowInformation implements WorkflowInformation<StateActivity>{
	
	private Document doc;
	
	private String tag;
	
	private String workflowId;
	
	public String getWorkflowId() {
		return workflowId;
	}

	public String getTag(){
		return this.tag;
	}
	
	public StateXmlFileWorkflowInformation(String tag,InputStream in) throws WorkflowRuntimeException {
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
	
	private void loadActivities(StateActivity act, Element ele,HashMap<String, StateActivity> items) throws WorkflowRuntimeException {
		if(!items.containsKey(act.getActivityId())){
			items.put(act.getActivityId(), act);
		}
		@SuppressWarnings("unchecked")
		List<Element> targets = ele.elements("target");
		if (targets.size() == 0 && act.getType() != StateActivity.END) {
			throw new WorkflowRuntimeException("状态流程中最终必须以End状态节点结尾:"
					+ act.getActivityId());
		}
		for (Element targetEle : targets) {
			Logic logic = null;
			String targetId = targetEle.attributeValue("to");
			if (targetId == null || "".equals(targetId.trim())) {
				throw new WorkflowRuntimeException("target中的目标不能为空!");
			}
			if (targetId.equals(act.getActivityId())) {
				throw new WorkflowRuntimeException("target中不能包含自引用:"+ targetId);
			}
			String targetSeqId = targetEle.attributeValue("ref");
			String importString = targetEle.attributeValue("import");
			String targetTagId = targetEle.attributeValue("id");
			String express = targetEle.getTextTrim();
			if (express == null || "".equals(express.trim())) {
				express = "true";
			}
			try {
				logic = Logic.getLogic("TARGET" + IDCreator.getDateString(),
						ILogicSource.chooseSource, importString, express,targetTagId);
			} catch (CompileException e) {
				throw new WorkflowRuntimeException("target条件编译失败:" + targetId);
			}
			StateActivity state = null;
			if(!items.containsKey(targetId)){
				Node targetNode = ele.getDocument().selectSingleNode(
						"/wf:workflow/wf:state[@id='" + targetId + "']");
				if (targetNode == null) {
					throw new WorkflowRuntimeException("没有找到target中指向的状态:"
							+ targetId);
				}
				Element targetElement = (Element) targetNode;
				state = XmlFileActivityConfig.getInstance().getActivityInstance(targetElement, StateActivity.class);
				this.loadActivities(state, targetElement,items);
			} else {
				state = items.get(targetId);
			}
			TargetStateEntity tse = new TargetStateEntity();
			tse.setTargetActivity(state);
			tse.setTargetExecutorId(targetSeqId);
			tse.setTargetLogic(logic);
			act.putTargetStateInfo(tse);
		}
	}
	
	@Override
	public WorkflowInstance<StateActivity> getItemById(String key)
			throws WorkflowRuntimeException {
		Element beginState = (Element)doc.selectSingleNode("/wf:workflow/wf:state[@type='begin']");
		StateActivity begin = XmlFileActivityConfig.getInstance().getActivityInstance(beginState,StateActivity.class);
		this.loadActivities(begin, beginState,new HashMap<String, StateActivity>());
		StateWorkflowInstance instance = new StateWorkflowInstance();
		instance.setRoot(begin);
		return instance;
	}

	@Override
	public WorkflowInstance<StateActivity>[] getItems(boolean isLoad)
			throws WorkflowRuntimeException {
		throw new WorkflowRuntimeException("状态工作流没有实现该方法");
	}
}
