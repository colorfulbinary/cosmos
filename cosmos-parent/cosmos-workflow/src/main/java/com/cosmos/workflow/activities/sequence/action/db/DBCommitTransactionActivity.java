package com.cosmos.workflow.activities.sequence.action.db;

import java.sql.SQLException;

import com.cosmos.utils.database.CommonDBAccess;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class DBCommitTransactionActivity extends ActionActivity{

	private static final long serialVersionUID = -5192576687673628325L;
	
	private String dbAccess;

	public String getDbAccess() {
		return dbAccess;
	}

	public void setDbAccess(String dbaccess) {
		this.dbAccess = dbaccess;
	}

	@Override
	public void release() {
		this.dbAccess = null;
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		Object dbo = null;
		if(this.dbAccess== null ||(dbo = data.get(this.dbAccess)) == null || !(dbo instanceof CommonDBAccess)){
			throw new WorkflowException(this,"无法找到DBAccess");
			
		}
		CommonDBAccess dba = (CommonDBAccess) dbo;
		try {
			dba.commit();
		} catch (SQLException e) {
			throw new WorkflowException(this,"动态执行错误:" + e.getMessage(),e);
		}
	}

}
