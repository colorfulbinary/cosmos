package com.cosmos.workflow.runtime.xml.initializer;

import org.dom4j.Element;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.sequence.action.db.DBQueryExecutorActivity;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.xml.IXmlInitializer;

public class DBQueryExecutorActivityInitializer implements IXmlInitializer<DBQueryExecutorActivity>{

	@Override
	public void init(DBQueryExecutorActivity activity, Element item)
			throws WorkflowRuntimeException {
//		String in = item.attributeValue("in");
//		activity.setIn(in);
		String out = item.attributeValue("out");
		activity.setOut(out);
		String processor = item.attributeValue("processor");
		String autoTransaction = item.attributeValue("auto-transaction");
		String sql = item.getTextTrim();
		String args = item.attributeValue("arguments");
		String access = item.attributeValue("access");
		activity.setSql(sql);
		activity.setProcessor(processor);
		activity.setAutoTransaction(autoTransaction != null && autoTransaction.trim().equals("false") ? false : true );
		if(args == null || args.trim().equals("")){
			activity.setArgs(new String[0]);
		} else {
			activity.setArgs(args.split(","));
		}
		if(access == null || access.trim().equals("")){
			throw new WorkflowRuntimeException("必须指定一个DBAccess");
		} else {
			activity.setDbAccess(access);
		}
		Element pager = item.element("pager");
		if(pager != null){
			String instance = pager.attributeValue("instance");
			String pageNumber = pager.attributeValue("page-number");
			String pageSize = pager.attributeValue("page-size");
			String countOut = pager.attributeValue("count-out");
			if(StringUtils.isEmptyOrNull(instance) 
					|| StringUtils.isEmptyOrNull(pageNumber) 
					|| StringUtils.isEmptyOrNull(pageSize) 
					|| StringUtils.isEmptyOrNull(countOut)){
				throw new WorkflowRuntimeException("分页信息不全");
			}
			activity.setPager(instance);
			activity.setPageNumber(pageNumber);
			activity.setPageSize(pageSize);
			activity.setCountOut(countOut);
		}
	}

}
