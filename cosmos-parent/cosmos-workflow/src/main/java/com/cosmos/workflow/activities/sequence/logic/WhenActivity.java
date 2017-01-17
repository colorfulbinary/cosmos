package com.cosmos.workflow.activities.sequence.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cosmos.utils.identity.IDCreator;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.SQActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.cosmos.workflow.logic.CompileException;
import com.cosmos.workflow.logic.ILogicSource;
import com.cosmos.workflow.logic.Logic;
public class WhenActivity extends LogicActivity{
	
	private static final long serialVersionUID = -8091030886433597224L;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getImportString() {
		return importString;
	}

	public void setImportString(String importString) {
		this.importString = importString;
	}

	private List<SQActivity> activities = new ArrayList<SQActivity>();
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public static final int WHEN = 0;
	
	public static final int OTHER = 1;
	
	private String condition;
	
	private String importString;
	
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void appendChild(SQActivity act) {
		this.activities.add(act);
	}

	@Override
	public void execute(ISequenceLogicData data) throws WorkflowException {
		for (int i = 0,len = this.activities.size(); i < len; i++) {
			try {
				activities.get(i).execute(data);
			} catch (WorkflowException e) {
				if(!this.isExceptionOnContinue()){
					throw e;
				}
			}
		}
	}
	
	public boolean check(ISequenceLogicData data) throws WorkflowException{
		Logic logic = this.getLogic();
		lock.readLock().lock();
		if(logic == null){
			lock.readLock().unlock();
			lock.writeLock().lock();
			try {
				logic = Logic.getLogic("WHEN" + IDCreator.getDateString(), ILogicSource.chooseSource, this.importString, this.condition,this.getActivityId());
				this.setLogic(logic);
			} catch (CompileException e) {
				throw new WorkflowException(this,"条件编译失败:" + e.getMessage());
			} finally {
				lock.readLock().lock();
				lock.writeLock().unlock();
			}
		}
		lock.readLock().unlock();
		boolean result = false;
		try {
			result = logic.execute(data).isSuccess();
		} catch (Exception e) {
			throw new WorkflowException(this,"条件执行失败:" + e.getMessage());
		}
		return result;
	}

	@Override
	public void release() {
		this.condition = null;
		this.importString = null;
		this.lock = null;
		for (SQActivity sqActivity : activities) {
			sqActivity.releaseResource();
		}
		this.activities.clear();
		this.activities = null;
	}

}
