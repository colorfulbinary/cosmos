package com.cosmos.workflow.activities.sequence.action.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cosmos.utils.database.CommonDBAccess;
import com.cosmos.utils.database.CommonDBPager;
import com.cosmos.utils.database.DataSet;
import com.cosmos.utils.database.IResultProcessor;
import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class DBQueryExecutorActivity extends ActionActivity{
	
	private static final long serialVersionUID = 7107234284856776840L;

	private String pager;
	
	private String pageNumber;
	
	private String pageSize;
	
	private String countOut;

	private boolean autoTransaction = true;
	
	private String processor;

	private String sql;
	
	private String[] args;
	
	private String dbAccess;
	
	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getDbAccess() {
		return dbAccess;
	}

	public void setDbAccess(String dbAccess) {
		this.dbAccess = dbAccess;
	}
	
	public boolean isAutoTransaction() {
		return autoTransaction;
	}

	public void setAutoTransaction(boolean autoTransaction) {
		this.autoTransaction = autoTransaction;
	}
	
	public String getPager() {
		return pager;
	}

	public void setPager(String pager) {
		this.pager = pager;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getCountOut() {
		return countOut;
	}

	public void setCountOut(String countOut) {
		this.countOut = countOut;
	}
	

	@Override
	public void release() {
		this.sql = null;
		this.args = null;
		this.dbAccess = null;
		this.processor = null;
	}
	
	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object dbo = null;
		Object processorObj = null;
		IResultProcessor<?> processor = null;
		Object pg = null;
		CommonDBPager pager = null;
		int pageNumber = 0;
		int pageSize = 0;
		if(this.dbAccess== null ||(dbo = data.get(this.dbAccess)) == null || !(dbo instanceof CommonDBAccess)){
			throw new WorkflowException(this,"无法找到DBAccess");
		}
		if(this.processor!= null && (processorObj = data.get(this.processor)) != null && processorObj instanceof IResultProcessor<?>){
			processor = (IResultProcessor<?>) processorObj;
		}
		if(this.pager != null &&(pg = data.get(this.pager)) != null && pg instanceof CommonDBPager){
			pager = (CommonDBPager) pg;
			String pn = data.getBoundValue(this.pageNumber);
			String ps = data.getBoundValue(this.pageSize);
			if(StringUtils.isEmptyOrNull(pn) || StringUtils.isEmptyOrNull(ps)){
				throw new WorkflowException(this, "没有到找分页信息");
			}
			pageNumber = Integer.valueOf(pn);
			pageSize = Integer.valueOf(ps);
		}
		List<Object> argsList = null;
		Object[] argsObj = null;
		if(args != null){
			argsList = new ArrayList<Object>(args.length);
			for (int i = 0 ,len = args.length; i < len; i++) {
				Object item = data.get(args[i]);
				if(item instanceof Object[]){
					Object[] itemList = (Object[]) item;
					for (int j = 0,jlen = itemList.length; j < jlen; j++) {
						argsList.add(itemList[j]);
					}
				} else {
					argsList.add(item);
				}
			}
		} 
		argsObj = argsList == null ? new Object[0] : argsList.toArray(new Object[0]);
		String sql = this.sql;
		if(sql.startsWith("{")){
			sql = data.get(sql.substring(1,sql.length() - 1)).toString();
		} 
		CommonDBAccess dba = (CommonDBAccess) dbo;
		try {
			Object queryResult = null;
			if(processor == null){
				DataSet res = dba.executeQuery(pager, pageNumber, pageSize, sql, argsObj);
				if(pager != null){
					if(!StringUtils.isEmptyOrNull(this.countOut)){
						data.put(this.countOut, res.getPager().getPageCount());
					}
				}
				queryResult = res.toMapArray();
			} else {
				queryResult = dba.executeQuery(processor, sql, argsObj);
			}
			data.put(this.out, queryResult);
		} catch (SQLException e) {
			throw new WorkflowException(this,"动态执行错误:" + e.getMessage() ,e);
		} finally{
			try {
				if(this.autoTransaction){
					dba.closeConnection();
				}
			} catch (SQLException e) {
			}
		}
	}
}
