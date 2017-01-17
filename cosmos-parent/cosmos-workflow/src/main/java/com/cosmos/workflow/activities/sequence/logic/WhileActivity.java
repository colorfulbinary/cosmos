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

public class WhileActivity extends LogicActivity{
	
	private static final long serialVersionUID = 2300485082118778415L;

	private String condition;
	
	private String importString;
	
	private List<SQActivity> activities = new ArrayList<SQActivity>();
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public void appendChild(SQActivity act) {
		activities.add(act);
	}

	@Override
	public void execute(ISequenceLogicData data) throws WorkflowException {
		Logic logic = this.getLogic();
		lock.readLock().lock();
		if(logic == null){
			lock.readLock().unlock();
			lock.writeLock().lock();
			try {
				logic = Logic.getLogic("WHILE" + IDCreator.getDateString(), ILogicSource.chooseSource, this.importString, this.condition,this.getActivityId());
				this.setLogic(logic);
			} catch (CompileException e) {
				throw new WorkflowException(this,"while条件编译失败:" + e.getMessage());
			} finally {
				lock.readLock().lock();
				lock.writeLock().unlock();
			}
		}
		lock.readLock().unlock();
		
		try {
			while (logic.execute(data).isSuccess()) {
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
		} catch (WorkflowException e) {
			throw e;
		} catch (Exception e) {
			throw new WorkflowException(this,"while条件执行失败:" + e.getMessage());
		}
		
		
	}

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
