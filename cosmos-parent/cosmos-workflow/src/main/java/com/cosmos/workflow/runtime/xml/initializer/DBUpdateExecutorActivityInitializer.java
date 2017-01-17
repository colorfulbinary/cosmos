package com.cosmos.workflow.runtime.xml.initializer;

import java.util.List;

import org.dom4j.Element;

import com.cosmos.workflow.activities.sequence.action.db.DBUpdateExecutorActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class DBUpdateExecutorActivityInitializer implements IXmlInitializer<DBUpdateExecutorActivity>{

	@Override
	public void init(DBUpdateExecutorActivity activity, Element item)
			throws WorkflowRuntimeException {
		String access = item.attributeValue("access");
		if(access == null || access.trim().equals("")){
			throw new WorkflowRuntimeException("必须指定一个DBAccess");
		} else {
			activity.setDbAccess(access);
		}
		String autoTransaction = item.attributeValue("auto-transaction");
		activity.setAutoTransaction(autoTransaction != null && autoTransaction.trim().equals("false") ? false : true );
		List<?> items = item.elements("item");
		if(items.size() == 0){
			throw new WorkflowRuntimeException("至少有一条要执行的SQL");
		} else {
			DBUpdateExecutorActivity.UpdateInfo[] infos = new DBUpdateExecutorActivity.UpdateInfo[items.size()];
			for (int i = 0,len = items.size(); i < len; i++) {
				Element ele = (Element) items.get(i);
				String sql = ele.getTextTrim();
				String args = ele.attributeValue("arguments");
				DBUpdateExecutorActivity.UpdateInfo info = activity.new UpdateInfo();
				info.setSql(sql);
				if(args == null || args.trim().equals("")){
					info.setArgs(new String[0]);
				} else {
					info.setArgs(args.split(","));
				}
				infos[i] = info;
			}
			activity.setInfo(infos);
		}
		
	}

}
