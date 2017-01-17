package com.cosmos.workflow.activities.sequence.action.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cosmos.utils.database.CommonDBAccess;
import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class DBUpdateExecutorActivity extends ActionActivity{
	
	private static final long serialVersionUID = -4288331494226062329L;

	private boolean autoTransaction = true;
	
	private String dbAccess;
	
	private UpdateInfo[] infos;
	
	public boolean isAutoTransaction() {
		return autoTransaction;
	}

	public void setAutoTransaction(boolean autoTransaction) {
		this.autoTransaction = autoTransaction;
	}
	
	public UpdateInfo[] getInfo() {
		return infos;
	}

	public void setInfo(UpdateInfo[] info) {
		this.infos = info;
	}

	public String getDbAccess() {
		return dbAccess;
	}

	public void setDbAccess(String dbAccess) {
		this.dbAccess = dbAccess;
	}

	public class UpdateInfo{
		
		private String sql;
		
		private String[] args;
		
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
	}
	@Override
	public void release() {
		this.dbAccess = null;
		if(this.infos != null && this.infos.length > 0){
			for (UpdateInfo info : infos) {
				info.setSql(null);
				info.setArgs(null);
					
			}
		}
	}
	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		if(this.infos == null || this.infos.length == 0){
			throw new WorkflowException(this,"没有要执行的语句");
		}
		Object dbo = data.get(this.dbAccess);
		if(dbo == null || !(dbo instanceof CommonDBAccess)){
			throw new WorkflowException(this,"无法找到DBAccess");
		}
		int result = 0;
		CommonDBAccess dba = (CommonDBAccess) dbo;
		for(int i=0,len = infos.length;i<len;i++){
			UpdateInfo item = this.infos[i];
			String[] args = item.getArgs();
			Object[] argsObj = null;
			List<Object> argsList = null;
			if(args != null){
				argsList = new ArrayList<Object>(args.length);
				for (int k = 0 ,klen = args.length; k < klen; k++) {
					Object uitem = data.get(args[k]);
					if(uitem instanceof Object[]){
						Object[] itemList = (Object[]) uitem;
						for (int j = 0,jlen = itemList.length; j < jlen; j++) {
							argsList.add(itemList[j]);
						}
					} else {
						argsList.add(uitem);
					}
				}
			} 
			argsObj = argsList == null ? new Object[0] : argsList.toArray(new Object[0]);
			try {
				result = dba.executeNoQuery(item.getSql(), argsObj);
			} catch (SQLException e) {
				try {
					if(this.autoTransaction){
						dba.rollback();
						dba.closeConnection();
					}
				} catch (SQLException e1) {
				}
				throw new WorkflowException(this,"动态执行错误:" + e.getMessage(),e);
			}
		}
		if(this.autoTransaction){
			try {
				dba.commit();
			} catch (SQLException e) {
				throw new WorkflowException(this,"事务提交失败：" + e.getMessage(),e);
			} finally{
				try {
					dba.closeConnection();
				} catch (SQLException e) {
				}
			}
		}
		if(!StringUtils.isEmptyOrNull(this.out)){
			data.put(this.out, result);
		}
	}

}
