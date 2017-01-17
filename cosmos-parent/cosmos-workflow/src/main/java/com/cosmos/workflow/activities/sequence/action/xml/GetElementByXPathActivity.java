package com.cosmos.workflow.activities.sequence.action.xml;

import java.util.List;

import org.dom4j.Node;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class GetElementByXPathActivity extends ActionActivity{
	
	private static final long serialVersionUID = -3333435557037988407L;

	private String xpath;
	
	private String countOut;
	
	private boolean first;
	

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getCountOut() {
		return countOut;
	}

	public void setCountOut(String countOut) {
		this.countOut = countOut;
	}
	
	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	@Override
	public void release() {
		this.xpath = null;
		this.countOut = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object obj =  data.get(this.in);
		if(obj == null || !(obj instanceof Node)){
			throw new WorkflowException(this,"必须指定一个Node");
		}
		Node doc = (Node) obj;
		String path = null;
		if(this.xpath.startsWith("{")){
			Object ph = data.get(this.xpath.substring(1,this.xpath.length() - 1));
			if(ph!=null && !ph.toString().trim().equals("")){
				path = ph.toString();
			} else {
				throw new WorkflowException(this,"找不到绑定文件路径");
			}
		} else {
			path = this.xpath;
		}
		List<?> selectNodes = doc.selectNodes(path);
		Object result = this.first ? (selectNodes.size() == 0 ? null : selectNodes.get(0)) : selectNodes;
		data.put(this.out, result);
		if(!StringUtils.isEmptyOrNull(this.countOut)){
			if(this.first){
				data.put(this.countOut, 1);
			} else {
				data.put(this.countOut, selectNodes.size());
			}
		} 
	}


}
